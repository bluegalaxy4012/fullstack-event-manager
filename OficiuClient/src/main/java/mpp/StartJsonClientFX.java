//package mpp;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import mpp.jsonprotocol.OficiuServicesJsonProxy;
//import mpp.service.OficiuServices;
//import mpp.gui.controller.LoginController;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Properties;
//
//public class StartJsonClientFX extends Application {
//
//    private static int defaultPort = 55555;
//    private static String defaultServer = "localhost";
//
//    private static Logger logger = LogManager.getLogger(StartJsonClientFX.class);
//
//    public void start(Stage primaryStage) throws Exception {
//        logger.debug("In start");
//        Properties clientProps = new Properties();
//        try {
//            clientProps.load(StartJsonClientFX.class.getResourceAsStream("/oficiuclient.properties"));
//            logger.info("Client properties set {} ", clientProps);
//            clientProps.list(System.out);
//        } catch (IOException e) {
//            logger.error("Cannot find oficiuclient.properties " + e);
//            logger.debug("Looking for oficiuclient.properties in folder {}", (new File(".")).getAbsolutePath());
//            return;
//        }
//        String serverIP = clientProps.getProperty("oficiu.server.host", defaultServer);
//        int serverPort = defaultPort;
//
//        try {
//            serverPort = Integer.parseInt(clientProps.getProperty("oficiu.server.port"));
//        } catch (NumberFormatException ex) {
//            logger.error("Wrong port number " + ex.getMessage());
//            logger.debug("Using default port: " + defaultPort);
//        }
//        logger.info("Using server IP " + serverIP);
//        logger.info("Using server port " + serverPort);
//
//        OficiuServices server = new OficiuServicesJsonProxy(serverIP, serverPort);
//
//        FXMLLoader loader = new FXMLLoader(
//                getClass().getClassLoader().getResource("loginwindow-view.fxml"));
//        Parent root = loader.load();
//
//        LoginController ctrl = loader.getController();
//        ctrl.setServer(server);
//
//        primaryStage.setTitle("Login (json)");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}