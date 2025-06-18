package mpp.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mpp.domain.Utilizator;
import mpp.service.OficiuException;
import mpp.service.OficiuServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.HashUtils;

import java.io.IOException;

public class LoginController {

    private OficiuServices server;
    private MainController mainCtrl;
    private Utilizator crtUser;

    private static Logger logger = LogManager.getLogger(LoginController.class);
    @FXML
    TextField numeUtilizatorTextField;
    @FXML
    TextField parolaTextField;

    public void setServer(OficiuServices s) {
        server = s;
    }


    @FXML
    public void handleLoginButton() throws IOException, OficiuException {
        String nume = numeUtilizatorTextField.getText();
        String passwd = parolaTextField.getText();

        try {
            FXMLLoader mainWindowLoader = new FXMLLoader(getClass().getResource("/mainwindow-view.fxml"));
            AnchorPane mainWindowLayout = mainWindowLoader.load();
            mainCtrl = mainWindowLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(mainWindowLayout));



            //System.out.println("username: " + nume + " password: " + passwd);

            String hashedPasswd = HashUtils.hashPassword(passwd);
            crtUser = new Utilizator(nume, hashedPasswd);
            Utilizator loggedInUser = server.login(crtUser, mainCtrl);

            if (loggedInUser == null) {
                throw new OficiuException("Nume utilizator sau parola gresita");
            }

            Stage s = (Stage) numeUtilizatorTextField.getScene().getWindow();
            s.close();

            stage.setTitle("Oficiu pt " + loggedInUser.getNumeUtilizator());

            stage.setOnCloseRequest((WindowEvent event) -> {
                try {
                    server.logout(loggedInUser);
                } catch (OficiuException e) {
                    throw new RuntimeException(e);
                }
                logger.debug("Closing application");
                System.exit(0);
            });

            stage.show();

            mainCtrl.setData(server, loggedInUser);

        } catch (OficiuException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Oficiu");
            alert.setHeaderText("Login esuat");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }



}