package mpp.utils;

import mpp.jsonprotocol.OficiuClientJsonWorker;
import mpp.service.OficiuServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public class OficiuJsonConcurrentServer extends AbsConcurrentServer {
    private final OficiuServices oficiuServer;
    private static final Logger logger = LogManager.getLogger(OficiuJsonConcurrentServer.class);

    public OficiuJsonConcurrentServer(int port, OficiuServices oficiuServer) {
        super(port);
        this.oficiuServer = oficiuServer;
        logger.info("Oficiu-OficiuJsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        OficiuClientJsonWorker worker = new OficiuClientJsonWorker(oficiuServer, client);
        return new Thread(worker);
    }
}