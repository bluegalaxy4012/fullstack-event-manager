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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OficiuProtoProxy implements OficiuServices {
    private String host;
    private int port;

    private OficiuObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<OficiuProtobuffs.OficiuResponse> qresponses;
    private volatile boolean finished;

    private static final Logger logger = LogManager.getLogger(OficiuProtoProxy.class);

    public OficiuProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    @Override
    public Utilizator login(Utilizator utilizator, OficiuObserver client) throws OficiuException {
        initializeConnection();

        System.out.println("BAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");

        OficiuProtobuffs.OficiuRequest req = ProtoUtils.createFindUtilizatorRequest(utilizator.getNumeUtilizator(), utilizator.getParola());
        sendRequest(req);
        OficiuProtobuffs.OficiuResponse response = readResponse();
        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }

        utilizator = ProtoUtils.getUtilizator(response);


        req = ProtoUtils.createLoginRequest(utilizator);
        sendRequest(req);
        response = readResponse();
        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.LOGIN_SUCCESS) {
            this.client = client;
            return ProtoUtils.getUtilizator(response);
        }
        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.ERROR) {

            throw new OficiuException("Utilizatorul este deja logat");
        }
        return null;
    }

    @Override
    public void logout(Utilizator user) throws OficiuException {
        logger.debug("Sending logout request");
        OficiuProtobuffs.OficiuRequest req = ProtoUtils.createLogoutRequest(user);
        sendRequest(req);
        OficiuProtobuffs.OficiuResponse response = readResponse();
        closeConnection();

        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
    }

    @Override
    public Optional<Participant> saveParticipant(Participant participant) throws OficiuException {

        OficiuProtobuffs.OficiuRequest req = ProtoUtils.createSaveParticipantRequest(participant);
        sendRequest(req);
        OficiuProtobuffs.OficiuResponse response = readResponse();
        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return Optional.ofNullable(ProtoUtils.getParticipant(response));
    }

    @Override
    public Iterable<Proba> findAllProbe() throws OficiuException {
        OficiuProtobuffs.OficiuRequest req = ProtoUtils.createFindAllProbeRequest();
        sendRequest(req);
        OficiuProtobuffs.OficiuResponse response = readResponse();
        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return List.of(ProtoUtils.getProbe(response));
    }

    @Override
    public Optional<Proba> findProbaById(Integer id) throws OficiuException {
        OficiuProtobuffs.OficiuRequest req = ProtoUtils.createFindProbaByIdRequest(id);
        sendRequest(req);
        OficiuProtobuffs.OficiuResponse response = readResponse();
        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return Optional.ofNullable(ProtoUtils.getProba(response));
    }

    @Override
    public int getNrParticipantiInscrisi(int idProba) throws OficiuException {
        OficiuProtobuffs.OficiuRequest req = ProtoUtils.createGetNrParticipantiInscrisiRequest(idProba);
        sendRequest(req);
        OficiuProtobuffs.OficiuResponse response = readResponse();
        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return response.getCount();
    }

    @Override
    public Iterable<Participant> findParticipantiByProba(int idProba) throws OficiuException {
        OficiuProtobuffs.OficiuRequest req = ProtoUtils.createFindParticipantiByProbaRequest(idProba);
        sendRequest(req);
        OficiuProtobuffs.OficiuResponse response = readResponse();
        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return List.of(ProtoUtils.getParticipanti(response));
    }

    @Override
    public Optional<Inscriere> saveInscriere(Inscriere inscriere) throws OficiuException {
        OficiuProtobuffs.OficiuRequest req = ProtoUtils.createSaveInscriereRequest(inscriere);
        sendRequest(req);
        OficiuProtobuffs.OficiuResponse response = readResponse();
        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return Optional.ofNullable(ProtoUtils.getInscriere(response));
    }

    @Override
    public int getNrProbeParticipant(int idParticipant) throws OficiuException {
        OficiuProtobuffs.OficiuRequest req = ProtoUtils.createGetNrProbeParticipantRequest(idParticipant);
        sendRequest(req);
        OficiuProtobuffs.OficiuResponse response = readResponse();
        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return response.getCount();
    }

    @Override
    public Utilizator findUtilizator(String username, String password) throws OficiuException {
        OficiuProtobuffs.OficiuRequest req = ProtoUtils.createFindUtilizatorRequest(username, password);
        sendRequest(req);
        OficiuProtobuffs.OficiuResponse response = readResponse();
        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.ERROR) {
            String err = response.getErrorMessage();
            throw new OficiuException(err);
        }
        return ProtoUtils.getUtilizator(response);
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

    private void sendRequest(OficiuProtobuffs.OficiuRequest request) throws OficiuException {
        try {
            logger.debug("Sending request: {}", request);
            request.writeDelimitedTo(output);
            output.flush();
        } catch (IOException e) {
            throw new OficiuException("Error sending request: " + e);
        }
    }

    private OficiuProtobuffs.OficiuResponse readResponse() throws OficiuException {
        OficiuProtobuffs.OficiuResponse response = null;
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
            output = connection.getOutputStream();
            input = connection.getInputStream();
            finished = false;
            startReader();
        } catch (IOException e) {
            throw new OficiuException("Error initializing connection: " + e);
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(OficiuProtobuffs.OficiuResponse response) {
        if (response.getType() == OficiuProtobuffs.OficiuResponse.Type.INSCRIERE_ADDED && client != null) {
            Inscriere inscriere = ProtoUtils.getInscriere(response);
            logger.debug("Inscriere added notification received: {}", inscriere);
            try {
                client.inscriereAdded(inscriere);
            } catch (Exception e) {
                logger.error("Error notifying client about inscriere: {}", e.getMessage());
            }
        }
    }

    private boolean isUpdate(OficiuProtobuffs.OficiuResponse.Type type) {
        return type == OficiuProtobuffs.OficiuResponse.Type.INSCRIERE_ADDED;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    OficiuProtobuffs.OficiuResponse response = OficiuProtobuffs.OficiuResponse.parseDelimitedFrom(input);
                    logger.debug("Response received: {}", response);

                    if (response == null) { break; }

                    if (isUpdate(response.getType())) {
                        handleUpdate(response);
                    } else {
                        try {
                            logger.debug("Putting response in queue: {}", response);
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            logger.error("Error putting response in queue: {}", e);
                        }
                    }
                } catch (IOException e) {
                    logger.error("Reading error: {}", e);
                }
            }
        }
    }
}