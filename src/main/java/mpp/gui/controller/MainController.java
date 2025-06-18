package mpp.gui.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mpp.domain.Inscriere;
import mpp.domain.Participant;
import mpp.domain.Proba;
import mpp.gui.MessageAlert;
import mpp.service.InscriereService;
import mpp.service.ParticipantService;
import mpp.service.ProbaService;
import mpp.service.UtilizatorService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainController {

    private UtilizatorService utilizatorService;
    private ParticipantService participantService;
    private ProbaService probaService;
    private InscriereService inscriereService;

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
    private TextField numeTextField, varstaTextField, probeTextField;

    @FXML
    public void initialize() {
        idProbaColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        distantaColumn.setCellValueFactory(new PropertyValueFactory<>("distanta"));
        stilColumn.setCellValueFactory(new PropertyValueFactory<>("stil"));
        nrInscrisiColumn.setCellValueFactory(cellData -> {
            Proba proba = cellData.getValue();
            int nrParticipanti = inscriereService.getNrParticipantiInscrisi(proba.getId());
            return new SimpleIntegerProperty(nrParticipanti).asObject();
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

    @FXML
    public void handleAfiseazaInscrisiButton() {
        List<Proba> probeList = probeTableView.getSelectionModel().getSelectedItems();
        if (probeList.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Selectati o proba");
            return;
        }

        Proba proba = probeList.get(0);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/participantiwindow-view.fxml"));
            AnchorPane root = loader.load();

            List<Participant> participanti = StreamSupport.stream(inscriereService.findParticipantiByProba(proba.getId()).spliterator(), false).toList();
            ProbaController controller = loader.getController();
            controller.setData(utilizatorService, participantService, probaService, inscriereService, participanti);

            Stage stage = new Stage();
            stage.setTitle("Participantii la proba " + proba.getId());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleInscrieButton() {
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
            MessageAlert.showErrorMessage(null, "Varsta trebuie sa fie un numar");
            return;
        }

        String[] probeIds = probeStr.split(",");
        List<Proba> probeList = new ArrayList<>();
        for (String idStr : probeIds) {
            try {
                int id = Integer.parseInt(idStr.trim());
                Optional<Proba> proba = probaService.findOne(id);
                if (proba.isEmpty()) {
                    MessageAlert.showErrorMessage(null, "Proba cu id-ul " + id + " nu exista");
                    return;
                }
                probeList.add(proba.get());
            } catch (NumberFormatException e) {
                MessageAlert.showErrorMessage(null, "Id-urile probelor trebuie sa fie numere");
                return;
            }
        }

        Participant participant = new Participant();
        participant.setNume(nume);
        participant.setVarsta(varsta);

        participantService.save(participant);

        Optional<Participant> savedParticipant = participantService.getParticipantMRU();
        if (savedParticipant.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Eroare la salvarea participantului");
            return;
        }

        for (Proba proba : probeList) {
            Inscriere inscriere = new Inscriere();
            inscriere.setParticipant(savedParticipant.get());
            inscriere.setProba(proba);
            inscriereService.save(inscriere);
        }

        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Inscriere", "Participantul a fost inscris cu succes");
    }

    @FXML
    public void handleLogoutButton() {
        Stage stage = (Stage) probeTableView.getScene().getWindow();
        stage.close();
    }


    public void setData(UtilizatorService utilizatorService, ParticipantService participantService, ProbaService probaService, InscriereService inscriereService) {
        this.utilizatorService = utilizatorService;
        this.participantService = participantService;
        this.probaService = probaService;
        this.inscriereService = inscriereService;

        List<Proba> probe = StreamSupport.stream(probaService.findAll().spliterator(), false).toList();
        probeTableView.setItems(FXCollections.observableList(probe));
    }
}
