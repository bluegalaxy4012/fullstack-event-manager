package mpp.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import mpp.gui.MessageAlert;
import mpp.service.InscriereService;
import mpp.service.ParticipantService;
import mpp.service.ProbaService;
import mpp.service.UtilizatorService;

import java.io.IOException;


public class LoginController {
    private UtilizatorService utilizatorService;
    private ParticipantService participantService;
    private ProbaService probaService;
    private InscriereService inscriereService;


    @FXML
    private TextField numeUtilizatorTextField, parolaTextField;

    @FXML
    public void handleLoginButton() throws IOException {
        String username = numeUtilizatorTextField.getText();
        String password = parolaTextField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Nu pot fi campuri goale");
            return;
        }

        if (utilizatorService.findUtilizator(username, password) == null) {
            MessageAlert.showErrorMessage(null, "Utilizator sau parola gresita");
            return;
        }

        Stage stage = (Stage) numeUtilizatorTextField.getScene().getWindow();
        stage.close();

        FXMLLoader mainWindowLoader = new FXMLLoader(getClass().getResource("/mainwindow-view.fxml"));
        AnchorPane mainWindowLayout = mainWindowLoader.load();

        Stage mainWindowStage = new Stage();
        mainWindowStage.setTitle("Oficiu");
        mainWindowStage.setScene(new Scene(mainWindowLayout));



        MainController mainController = mainWindowLoader.getController();
        mainController.setData(utilizatorService, participantService, probaService, inscriereService);
        mainWindowStage.show();




    }

    public void setData(UtilizatorService utilizatorService, ParticipantService participantService, ProbaService probaService, InscriereService inscriereService) {
        this.utilizatorService = utilizatorService;
        this.participantService = participantService;
        this.probaService = probaService;
        this.inscriereService = inscriereService;

    }
}
