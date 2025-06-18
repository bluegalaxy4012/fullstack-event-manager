package mpp;

import mpp.repository.InscriereHibernateRepository;
import mpp.repository.ParticipantHibernateRepository;
import mpp.repository.ProbaHibernateRepository;
import mpp.repository.UtilizatorHibernateRepository;
//import mpp.repository.InscriereRepository;
//import mpp.repository.ProbaRepository;
//import mpp.repository.ParticipantRepository;
//import mpp.repository.UtilizatorRepository;
import mpp.repository.database.*;
import mpp.utils.AbstractServer;
import mpp.utils.OficiuProtobuffConcurrentServer;
import mpp.utils.ServerException;
import mpp.server.OficiuServicesImpl;
import mpp.service.OficiuServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class StartProtobuffServer {
    private static int defaultPort = 55555;
    private static final Logger logger = LogManager.getLogger(StartProtobuffServer.class);

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartProtobuffServer.class.getResourceAsStream("/oficiuserver.properties"));
            logger.info("Server properties set. {}", serverProps);
        } catch (IOException e) {
            logger.error("Cannot find oficiuserver.properties " + e);
            logger.debug("Looking for file in " + (new File(".")).getAbsolutePath());
            return;
        }

        //ParticipantRepository participantRepo = new ParticipantDbRepository(serverProps);
        //ParticipantHibernateRepository participantRepo = new ParticipantHibernateDbRepository(serverProps);
        ParticipantHibernateRepository participantRepo = new ParticipantHibernateDbRepository();

        //ProbaHibernateRepository probaRepo = new ProbaHibernateDbRepository(serverProps);
        ProbaHibernateRepository probaRepo = new ProbaHibernateDbRepository();

        //UtilizatorRepository utilizatorRepo = new UtilizatorDbRepository(serverProps);
        //UtilizatorHibernateRepository utilizatorRepo = new UtilizatorHibernateDbRepository(serverProps);
        UtilizatorHibernateRepository utilizatorRepo = new UtilizatorHibernateDbRepository();

        //InscriereHibernateRepository inscriereRepo = new InscriereHibernateDbRepository(serverProps);
        InscriereHibernateRepository inscriereRepo = new InscriereHibernateDbRepository();

        OficiuServices OficiuServer = new OficiuServicesImpl(participantRepo, probaRepo, utilizatorRepo, inscriereRepo);

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("oficiu.server.port"));
        } catch (NumberFormatException nef) {
            logger.error("Wrong Port Number " + nef.getMessage());
            logger.debug("Using default port " + defaultPort);
        }

        logger.debug("Starting server on port: " + serverPort);
        AbstractServer server = new OficiuProtobuffConcurrentServer(serverPort, OficiuServer);
        try {
            server.start();
        } catch (ServerException e) {
            logger.error("Error starting the server " + e.getMessage());
        }
    }
}