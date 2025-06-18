package mpp.jsonprotocol;

import mpp.domain.*;
import mpp.dto.DTOUtils;
import mpp.service.OficiuException;
import mpp.service.OficiuObserver;
import mpp.service.OficiuServices;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OficiuServicesJsonProxy implements OficiuServices {
    private String host;
    private int port;

    private OficiuObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    private static Logger logger = LogManager.getLogger(OficiuServicesJsonProxy.class);

    public OficiuServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    @Override
    public Utilizator login(Utilizator utilizator, OficiuObserver client) throws OficiuException {

        initializeConnection();
        Request req = JsonProtocolUtils.createFindUtilizatorRequest(utilizator.getNumeUtilizator(), utilizator.getParola());
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }

        utilizator = DTOUtils.getFromDTO(response.getUtilizator());



        req = JsonProtocolUtils.createLoginRequest(utilizator);
        sendRequest(req);
        response = readResponse();
        if (response.getType() == ResponseType.LOGIN_SUCCESS) {
            this.client = client;
            return DTOUtils.getFromDTO(response.getUtilizator());
        }
        if (response.getType() == ResponseType.ERROR) {

            String err = "Utilizatorul este deja logat";
            //
            throw new OficiuException(err);
        }
        return null;
    }

    @Override
    public void logout(Utilizator user) throws OficiuException {
        System.out.println("sending logout req");
        Request req = JsonProtocolUtils.createLogoutRequest(user);
        sendRequest(req);
        Response response = readResponse();
        closeConnection();

        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }

    }

    @Override
    public Optional<Participant> saveParticipant(Participant participant) throws OficiuException {

        Request req = JsonProtocolUtils.createSaveParticipantRequest(participant);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return Optional.ofNullable(DTOUtils.getFromDTO(response.getParticipant()));
    }


    @Override
    public Iterable<Proba> findAllProbe() throws OficiuException {

        Request req = JsonProtocolUtils.createFindAllProbeRequest();
        sendRequest(req);

        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return List.of(DTOUtils.getFromDTO(response.getProbe()));
    }

    @Override
    public Optional<Proba> findProbaById(Integer id) throws OficiuException {

        Request req = JsonProtocolUtils.createFindProbaByIdRequest(id);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return Optional.ofNullable(DTOUtils.getFromDTO(response.getProba()));
    }

    @Override
    public int getNrParticipantiInscrisi(int idProba) throws OficiuException {

        Request req = JsonProtocolUtils.createGetNrParticipantiInscrisiRequest(idProba);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return response.getCount();
    }

    @Override
    public Iterable<Participant> findParticipantiByProba(int idProba) throws OficiuException {

        Request req = JsonProtocolUtils.createFindParticipantiByProbaRequest(idProba);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return List.of(DTOUtils.getFromDTO(response.getParticipanti()));
    }

    @Override
    public Optional<Inscriere> saveInscriere(Inscriere inscriere) throws OficiuException {

        Request req = JsonProtocolUtils.createSaveInscriereRequest(inscriere);
        sendRequest(req);

        //Response tempresponse = readResponse();
        Response response = readResponse();

        //System.out.println("response = " + response + " request = " + req);

        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return Optional.ofNullable(DTOUtils.getFromDTO(response.getInscriere()));
    }

    @Override
    public int getNrProbeParticipant(int idParticipant) throws OficiuException {

        Request req = JsonProtocolUtils.createGetNrProbeParticipantRequest(idParticipant);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return response.getCount();
    }

    @Override
    public Utilizator findUtilizator(String username, String password) throws OficiuException {

        Request req = JsonProtocolUtils.createFindUtilizatorRequest(username, password);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return DTOUtils.getFromDTO(response.getUtilizator());
    }

    private void closeConnection() {

        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void sendRequest(Request request) throws OficiuException {
        String reqLine = gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new OficiuException("Error sending object " + e);
        }
    }

    private Response readResponse() throws OficiuException {
        Response response = null;
        try {
            response = qresponses.take();

        } catch (InterruptedException e) {
            logger.error(e);
        }
        return response;
    }

    private void initializeConnection() throws OficiuException {
        try {
            connection = new Socket(host, port);
            output = new PrintWriter(connection.getOutputStream(), true);
            gsonFormatter = new Gson();
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        } catch (IOException e) {
            throw new OficiuException("Error initializing connection " + e);
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response) {
        if (response.getType() == ResponseType.INSCRIERE_ADDED && client != null) {
            Inscriere inscriere = DTOUtils.getFromDTO(response.getInscriere());
            logger.debug("Inscriere added notification received: {}", inscriere);
            try {
                client.inscriereAdded(inscriere);
            } catch (Exception e) {
                logger.error("Error notifying client about inscriere: {}", e.getMessage());
            }
        }
    }


    private boolean isUpdate(Response response) {

        return response.getType() == ResponseType.INSCRIERE_ADDED;
    }

    private class ReaderThread implements Runnable {
        public void run() {
//            if(finished)
//                System.out.println("finished thread " + Thread.currentThread().getName());

            while (!finished) {
                try {
                    String responseLine = input.readLine();

                    logger.debug("response received {}", responseLine);
                    Response response = gsonFormatter.fromJson(responseLine, Response.class);

                    if(response == null) { break; }

                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {
                        try {
                            logger.debug("putting response in queue {}", response);
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            logger.error("Error putting response in queue {}", e);
                            logger.error(e);
                        }
                    }
                } catch (IOException e) {
                    logger.error("Reading error " + e);
                }
            }
        }
    }
}