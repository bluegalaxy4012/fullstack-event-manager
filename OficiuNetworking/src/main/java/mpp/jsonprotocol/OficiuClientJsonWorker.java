package mpp.jsonprotocol;

import com.google.gson.Gson;
import mpp.domain.*;
import mpp.dto.DTOUtils;
import mpp.service.OficiuObserver;
import mpp.service.OficiuServices;
import mpp.service.OficiuException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OficiuClientJsonWorker implements Runnable, OficiuObserver {
    private static final Logger logger = LogManager.getLogger(OficiuClientJsonWorker.class);
    private final OficiuServices server;
    private final Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private boolean connected;
    private Gson gsonFormatter;

    public OficiuClientJsonWorker(OficiuServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        this.gsonFormatter = new Gson();
        try {
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            output = new PrintWriter(connection.getOutputStream(), true);
            connected = true;
        } catch (IOException e) {
            logger.error("Error initializing worker: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                String requestLine = input.readLine();
                if (requestLine != null) {
                    Request request = gsonFormatter.fromJson(requestLine, Request.class);
                    Response response = handleRequest(request);
                    if (response != null) {
                        sendResponse(response);
                    }
                }
            } catch (IOException e) {
                logger.error(e);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error closing connection: " + e.getMessage());
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;
        try {
            switch (request.getType()) {
                case LOGIN:
                    Utilizator utilizator = DTOUtils.getFromDTO(request.getUtilizator());
                    Utilizator loggedInUser = server.login(utilizator, this);
                    response = JsonProtocolUtils.createUtilizatorLoggedInResponse(loggedInUser);
                    break;
                case LOGOUT:
                    Utilizator user = DTOUtils.getFromDTO(request.getUtilizator());
                    server.logout(user);
                    connected = false;
                    response = JsonProtocolUtils.createOkResponse();
                    break;
                case SAVE_PARTICIPANT:
                    Participant participant = DTOUtils.getFromDTO(request.getParticipant());
                    Optional<Participant> p = server.saveParticipant(participant);
                  response = JsonProtocolUtils.createParticipantSavedResponse(p.orElse(null));
                    break;

                case FIND_ALL_PROBE:
                    Iterable<Proba> probe = server.findAllProbe();
                    List<Proba> probeList = StreamSupport.stream(probe.spliterator(), false).collect(Collectors.toList());
                    Proba[] probeArray = probeList.toArray(new Proba[0]);
                    response = JsonProtocolUtils.createAllProbeResponse(probeArray);
                    break;

                case FIND_PROBA_BY_ID:
                    Proba proba = server.findProbaById(request.getProba().getId()).orElse(null);
                    response = JsonProtocolUtils.createProbaFoundResponse(proba);
                    break;
                case GET_NR_PARTICIPANTI_INSCRISI:
                    int nrParticipanti = server.getNrParticipantiInscrisi(request.getProba().getId());
                    response = JsonProtocolUtils.createNrParticipantiInscrisiResponse(nrParticipanti);
                    break;
                case FIND_PARTICIPANTI_BY_PROBA:
                    Iterable<Participant> participanti = server.findParticipantiByProba(request.getProba().getId());
                    List<Participant> participantiList = StreamSupport.stream(participanti.spliterator(), false).collect(Collectors.toList());
                    Participant[] participantiArray = participantiList.toArray(new Participant[0]);
                    response = JsonProtocolUtils.createParticipantiByProbaResponse(participantiArray);
                    break;
                case SAVE_INSCRIERE:
                    Inscriere inscriere = DTOUtils.getFromDTO(request.getInscriere());
                    //System.out.println("eeea" + inscriere);
                    server.saveInscriere(inscriere);
                    response = JsonProtocolUtils.createInscriereSavedResponse(inscriere);
                    break;
                case GET_NR_PROBE_PARTICIPANT:
                    int nrProbe = server.getNrProbeParticipant(request.getParticipant().getId());
                    response = JsonProtocolUtils.createNrProbeParticipantResponse(nrProbe);
                    break;
                case FIND_UTILIZATOR:
                    Utilizator utilizatorToFind = DTOUtils.getFromDTO(request.getUtilizator());
                    Utilizator foundUser = server.findUtilizator(utilizatorToFind.getNumeUtilizator(), utilizatorToFind.getParola());
                    if (foundUser == null) {
                        response = JsonProtocolUtils.createErrorResponse("Nume utilizator sau parola gresite");
                    } else {
                        response = JsonProtocolUtils.createUtilizatorLoggedInResponse(foundUser);
                    }
                    break;
                default:
                    response = JsonProtocolUtils.createErrorResponse("unknown request");
            }
        } catch (OficiuException e) {
            response = JsonProtocolUtils.createErrorResponse(e.getMessage());
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException {
        String responseLine = gsonFormatter.toJson(response);
        logger.debug("Sending response: " + responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }


    public void inscriereAdded(Inscriere inscriere) {
        try {
            Response resp = new Response();
            resp.setType(ResponseType.INSCRIERE_ADDED);
            resp.setInscriere(DTOUtils.getDTO(inscriere));

            logger.debug("Sending inscriere notification: {}", inscriere);
            sendResponse(resp);
        } catch (IOException e) {
            logger.error("Error sending inscriere notification: " + e.getMessage(), e);
        }
    }
}