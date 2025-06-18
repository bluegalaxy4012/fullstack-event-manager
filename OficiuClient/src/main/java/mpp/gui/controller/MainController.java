package mpp.gui.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mpp.domain.Inscriere;
import mpp.domain.Proba;
import mpp.domain.Participant;
import mpp.domain.Utilizator;
import mpp.gui.MessageAlert;
import mpp.service.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainController implements OficiuObserver {
    @FXML
    private TableView<Proba> probeTableView;
    @FXML
    private TableColumn<Proba, Integer> idProbaColumn;
    @FXML
    private TableColumn<Proba, String> distantaColumn;
    @FXML
    private TableColumn<Proba, String> stilColumn;
    @FXML
    private TableColumn<Proba, Integer> nrInscrisiColumn;
    @FXML
    private TextField numeTextField;
    @FXML
    private TextField varstaTextField;
    @FXML
    private TextField probeTextField;

    private List<ProbaController> probaControllers = new ArrayList<>();
    private Utilizator utilizator;




    private OficiuServices server;

    public void setData(OficiuServices server, Utilizator utilizator) throws OficiuException {
        this.server = server;
        this.utilizator = utilizator;
        loadProbe();
    }

    @FXML
    public void initialize() {
        idProbaColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        distantaColumn.setCellValueFactory(new PropertyValueFactory<>("distanta"));
        stilColumn.setCellValueFactory(new PropertyValueFactory<>("stil"));
        nrInscrisiColumn.setCellValueFactory(cellData -> {
            Proba proba = cellData.getValue();
            int nrInscrisi = 0;
            try {
                nrInscrisi = server.getNrParticipantiInscrisi(proba.getId());
            } catch (OficiuException e) {
                throw new RuntimeException(e);
            }
            return new SimpleIntegerProperty(nrInscrisi).asObject();
        });

        probeTableView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        probeTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Proba>) change -> {
            List<Proba> selectedProbe = probeTableView.getSelectionModel().getSelectedItems();
            String ids = selectedProbe.stream()
                    .map(proba -> String.valueOf(proba.getId()))
                    .collect(Collectors.joining(","));
            probeTextField.setText(ids);
        });


    }

    private void loadProbe() throws OficiuException {
        Iterable<Proba> probe = server.findAllProbe();
        ObservableList<Proba> probeList = FXCollections.observableArrayList();
        probe.forEach(probeList::add);
        probeTableView.setItems(probeList);
    }

    @FXML
    private void handleAfiseazaInscrisiButton() {
        Proba selectedProba = probeTableView.getSelectionModel().getSelectedItem();
        if (selectedProba != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/participantiwindow-view.fxml"));
                AnchorPane root = loader.load();

                ProbaController controller = loader.getController();
                controller.setData(server, selectedProba);
                probaControllers.add(controller);

                Stage stage = new Stage();
                stage.setTitle("Participantii la proba " + selectedProba.getId());
                stage.setScene(new Scene(root));

                stage.setOnCloseRequest(event -> {
                    probaControllers.remove(controller);
                });
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OficiuException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @FXML
    private void handleInscrieButton() {
        String nume = numeTextField.getText();
        String varstaStr = varstaTextField.getText();
        String probeStr = probeTextField.getText();

        if (nume.isEmpty() || varstaStr.isEmpty() || probeStr.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Toate campurile trebuie completate");
            return;
        }

        int varsta;
        try {
            varsta = Integer.parseInt(varstaStr);
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null, "Varsta trebuie să fie un numar");
            return;
        }

        String[] probeIds = probeStr.split(",");
        List<Proba> probeList = new ArrayList<>();
        for (String idStr : probeIds) {
            try {
                int id = Integer.parseInt(idStr.trim());
                Optional<Proba> proba = server.findProbaById(id);
                if (proba.isEmpty()) {
                    MessageAlert.showErrorMessage(null, "Proba cu id-ul " + id + " nu exista");
                    return;
                }
                probeList.add(proba.get());
            } catch (NumberFormatException e) {
                MessageAlert.showErrorMessage(null, "Id-urile probelor trebuie sa fie numere");
                return;
            } catch (OficiuException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Participant participant = new Participant(nume, varsta);

            Optional<Participant> savedParticipant = server.saveParticipant(participant);
            if (savedParticipant.isEmpty()) {
                MessageAlert.showErrorMessage(null, "Eroare la salvarea participantului");
                return;
            }

            for (Proba proba : probeList) {
                Inscriere inscriere = new Inscriere();
                inscriere.setParticipant(savedParticipant.get());
                inscriere.setProba(proba);
                server.saveInscriere(inscriere);
            }

            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Inscriere", "Participant inscris cu succes");
        } catch (OficiuException e) {
            MessageAlert.showErrorMessage(null, "Eroare: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogoutButton() throws OficiuException {
        server.logout(utilizator);
        Stage stage = (Stage) probeTableView.getScene().getWindow();
        stage.close();
    }


    @Override
    public void inscriereAdded(Inscriere inscriere) {
        Platform.runLater(() -> {

            try { loadProbe(); } catch (OficiuException e) {
                MessageAlert.showErrorMessage(null, "Eroare la incarcarea probelor");
            }
            for (ProbaController ctrl : probaControllers) {
                try {
                    if (inscriere.getProba().getId() == ctrl.getSelectedProba().getId())
                        ctrl.loadData();
                } catch (OficiuException e) {
                    MessageAlert.showErrorMessage(null, "Eroare la incarcarea participantilor");
                }
            }
        });
    }

}