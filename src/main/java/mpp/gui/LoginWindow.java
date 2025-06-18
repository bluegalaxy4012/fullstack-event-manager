package mpp.gui;

import mpp.gui.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mpp.repository.InscriereRepository;
import mpp.repository.ParticipantRepository;
import mpp.repository.ProbaRepository;
import mpp.repository.UtilizatorRepository;
import mpp.repository.database.InscriereDbRepository;
import mpp.repository.database.ParticipantDbRepository;
import mpp.repository.database.ProbaDbRepository;
import mpp.repository.database.UtilizatorDbRepository;
import mpp.service.InscriereService;
import mpp.service.ParticipantService;
import mpp.service.ProbaService;
import mpp.service.UtilizatorService;

import java.io.IOException;

import static mpp.Main.props;

public class LoginWindow extends Application {

    private UtilizatorService utilizatorService;
    private ParticipantService participantService;
    private ProbaService probaService;
    private InscriereService inscriereService;


    @Override
    public void start(Stage primaryStage) throws IOException {

        UtilizatorRepository utilizatorRepo = new UtilizatorDbRepository(props);
        utilizatorService = new UtilizatorService(utilizatorRepo);
        ParticipantRepository participantRepo = new ParticipantDbRepository(props);
        participantService = new ParticipantService(participantRepo);
        ProbaRepository probaRepo = new ProbaDbRepository(props);
        probaService = new ProbaService(probaRepo);
        InscriereRepository inscriereRepo = new InscriereDbRepository(props, participantRepo, probaRepo);
        inscriereService = new InscriereService(inscriereRepo);

        primaryStage.setTitle("Login");

        FXMLLoader loginWindowLoader = new FXMLLoader(LoginWindow.class.getResource("/loginwindow-view.fxml"));
        AnchorPane loginWindowLayout = loginWindowLoader.load();

        //Stage loginWindowStage = new Stage();
        primaryStage.setScene(new Scene(loginWindowLayout));
        //loginWindowStage.setScene(new Scene(loginWindowLayout));


        LoginController loginController = loginWindowLoader.getController();
        loginController.setData(utilizatorService, participantService, probaService, inscriereService);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}