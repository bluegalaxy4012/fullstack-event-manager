//package mpp;
//
//import mpp.repository.InscriereRepository;
//import mpp.repository.ParticipantRepository;
//import mpp.repository.ProbaRepository;
//import mpp.repository.UtilizatorRepository;
//import mpp.utils.AbstractServer;
//import mpp.utils.ServerException;
//import mpp.utils.OficiuJsonConcurrentServer;
//import mpp.repository.database.InscriereDbRepository;
//import mpp.repository.database.ParticipantDbRepository;
//import mpp.repository.database.ProbaDbRepository;
//import mpp.repository.database.UtilizatorDbRepository;
//import mpp.server.OficiuServicesImpl;
//import mpp.service.OficiuServices;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Properties;
//
//public class StartJsonServer {
//    private static int defaultPort = 55555;
//    private static final Logger logger = LogManager.getLogger(StartJsonServer.class);
//
//    public static void main(String[] args) {
//        //System.out.println("salut");
//        Properties serverProps = new Properties();
//        try {
//            serverProps.load(StartJsonServer.class.getResourceAsStream("/oficiuserver.properties"));
//            logger.info("Server properties set. {}", serverProps);
//        } catch (IOException e) {
//            logger.error("Cannot find oficiuserver.properties " + e);
//            logger.debug("Looking for file in " + (new File(".")).getAbsolutePath());
//            return;
//        }
//
//        ParticipantRepository participantRepo = new ParticipantDbRepository(serverProps);
//        ProbaRepository probaRepo = new ProbaDbRepository(serverProps);
//        UtilizatorRepository utilizatorRepo = new UtilizatorDbRepository(serverProps);
//        InscriereRepository inscriereRepo = new InscriereDbRepository(serverProps, participantRepo, probaRepo);
//
//        OficiuServices OficiuServer = new OficiuServicesImpl(participantRepo, probaRepo, utilizatorRepo, inscriereRepo);
//
//        int serverPort = defaultPort;
//        try {
//            serverPort = Integer.parseInt(serverProps.getProperty("oficiu.server.port"));
//        } catch (NumberFormatException nef) {
//            logger.error("Wrong Port Number " + nef.getMessage());
//            logger.debug("Using default port " + defaultPort);
//        }
//
//        logger.debug("Starting server on port: " + serverPort);
//        AbstractServer server = new OficiuJsonConcurrentServer(serverPort, OficiuServer);
//        try {
//            server.start();
//        } catch (ServerException e) {
//            logger.error("Error starting the server " + e.getMessage());
//        }
//    }
//}