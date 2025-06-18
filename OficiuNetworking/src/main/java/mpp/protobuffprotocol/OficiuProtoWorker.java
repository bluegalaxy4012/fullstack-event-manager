package mpp.protobuffprotocol;

import mpp.domain.*;
import mpp.service.OficiuException;
import mpp.service.OficiuObserver;
import mpp.service.OficiuServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OficiuProtoWorker implements Runnable, OficiuObserver {



    private static final Logger logger = LogManager.getLogger(OficiuProtoWorker.class);
    private final OficiuServices server;
    private final Socket connection;

    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;

    public OficiuProtoWorker(OficiuServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            input = connection.getInputStream();
            output = connection.getOutputStream();
            connected = true;
        } catch (IOException e) {
            logger.error("Error initializing worker: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                logger.debug("Waiting for request...");
                OficiuProtobuffs.OficiuRequest request = OficiuProtobuffs.OficiuRequest.parseDelimitedFrom(input);
                logger.debug("Request received: {}", request);

                if (request == null) {
                    break;
                }

                OficiuProtobuffs.OficiuResponse response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                logger.error("Error processing request: {}", e);
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

    private OficiuProtobuffs.OficiuResponse handleRequest(OficiuProtobuffs.OficiuRequest request) {
        OficiuProtobuffs.OficiuResponse response = null;
        try {
            switch (request.getType()) {
                case LOGIN:
                    Utilizator utilizator = ProtoUtils.getUtilizator(request.getUtilizator());

                    System.out.println("\n\n\n\n" + utilizator.getNumeUtilizator() + " " + utilizator.getParola() + "\n\n\n\n");
                    Utilizator loggedInUser = server.login(utilizator, this);
                    System.out.println("\n\n\n\n" + loggedInUser.getNumeUtilizator() + " " + loggedInUser.getParola() + "\n\n\n\n");

                    response = ProtoUtils.createUtilizatorLoggedInResponse(loggedInUser);
                    System.out.println("---------------\n\n\n\n" + response.getUtilizator() + "\n\n\n\n---------------");
                    break;
                case LOGOUT:
                    Utilizator user = ProtoUtils.getUtilizator(request.getUtilizator());
                    server.logout(user);
                    connected = false;
                    response = ProtoUtils.createOkResponse();
                    break;
                case SAVE_PARTICIPANT:
                    Participant participant = ProtoUtils.getParticipant(request.getParticipant());
                    Optional<Participant> p = server.saveParticipant(participant);
                    response = ProtoUtils.createParticipantSavedResponse(p.orElse(null));
                    break;
                case FIND_ALL_PROBE:
                    Iterable<Proba> probe = server.findAllProbe();
                    List<Proba> probeList = StreamSupport.stream(probe.spliterator(), false).collect(Collectors.toList());
                    Proba[] probeArray = probeList.toArray(new Proba[0]);
                    response = ProtoUtils.createAllProbeResponse(probeArray);
                    break;
                case FIND_PROBA_BY_ID:
                    Proba proba = server.findProbaById(request.getProba().getId()).orElse(null);
                    response = ProtoUtils.createProbaFoundResponse(proba);
                    break;
                case GET_NR_PARTICIPANTI_INSCRISI:
                    int nrParticipanti = server.getNrParticipantiInscrisi(request.getProba().getId());
                    response = ProtoUtils.createNrParticipantiInscrisiResponse(nrParticipanti);
                    break;
                case FIND_PARTICIPANTI_BY_PROBA:
                    Iterable<Participant> participanti = server.findParticipantiByProba(request.getProba().getId());
                    List<Participant> participantiList = StreamSupport.stream(participanti.spliterator(), false).collect(Collectors.toList());
                    Participant[] participantiArray = participantiList.toArray(new Participant[0]);
                    response = ProtoUtils.createParticipantiByProbaResponse(participantiArray);
                    break;
                case SAVE_INSCRIERE:
                    Inscriere inscriere = ProtoUtils.getInscriere(request.getInscriere());
                    server.saveInscriere(inscriere);
                    response = ProtoUtils.createInscriereSavedResponse(inscriere);
                    break;
                case GET_NR_PROBE_PARTICIPANT:
                    int nrProbe = server.getNrProbeParticipant(request.getParticipant().getId());
                    response = ProtoUtils.createNrProbeParticipantResponse(nrProbe);
                    break;
                case FIND_UTILIZATOR:
                    Utilizator utilizatorToFind = ProtoUtils.getUtilizator(request.getUtilizator());
                    Utilizator foundUser = server.findUtilizator(utilizatorToFind.getNumeUtilizator(), utilizatorToFind.getParola());
                    if (foundUser == null) {
                        response = ProtoUtils.createErrorResponse("Nume utilizator sau parola gresite");
                    } else {
                        response = ProtoUtils.createUtilizatorLoggedInResponse(foundUser);
                    }
                    break;
                default:
                    response = ProtoUtils.createErrorResponse("Unknown request type");
            }
        } catch (OficiuException e) {
            response = ProtoUtils.createErrorResponse(e.getMessage());
        }
        return response;
    }

    private void sendResponse(OficiuProtobuffs.OficiuResponse response) throws IOException {

        logger.debug("Sending response: {}", response);
        synchronized (output) {
            response.writeDelimitedTo(output);
            output.flush();
        }
    }

    @Override
    public void inscriereAdded(Inscriere inscriere) {

        try {

            OficiuProtobuffs.OficiuResponse response = ProtoUtils.createInscriereAddedResponse(inscriere);
            logger.debug("Sending inscriere notification: {}", inscriere);
            sendResponse(response);
        } catch (IOException e) {
            logger.error("Error sending inscriere notification: " + e.getMessage());
        }
    }
}