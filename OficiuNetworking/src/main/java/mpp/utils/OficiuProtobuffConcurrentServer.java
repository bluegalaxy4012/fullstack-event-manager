package mpp.utils;

import mpp.protobuffprotocol.OficiuProtoWorker;
import mpp.service.OficiuServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public class OficiuProtobuffConcurrentServer extends AbsConcurrentServer {
    private final OficiuServices oficiuServer;
    private static final Logger logger = LogManager.getLogger(OficiuProtobuffConcurrentServer.class);

    public OficiuProtobuffConcurrentServer(int port, OficiuServices oficiuServer) {
        super(port);
        this.oficiuServer = oficiuServer;
        logger.info("Oficiu-OficiuProtobuffConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        OficiuProtoWorker worker = new OficiuProtoWorker(oficiuServer, client);
        return new Thread(worker);
    }
}