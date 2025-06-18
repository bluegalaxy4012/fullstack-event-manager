package mpp.gui.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import mpp.domain.Participant;
import mpp.service.InscriereService;
import mpp.service.ParticipantService;
import mpp.service.ProbaService;
import mpp.service.UtilizatorService;

import java.util.List;

public class ProbaController {
    private UtilizatorService utilizatorService;
    private ParticipantService participantService;
    private ProbaService probaService;
    private InscriereService inscriereService;

    @FXML
    private TableView<Participant> participantiTableView;
    @FXML
    private TableColumn<Participant, String> idParticipantColumn;
    @FXML
    private TableColumn<Participant, Integer> varstaColumn;
    @FXML
    private TableColumn<Participant, Integer> nrProbeColumn;


    public void setData(UtilizatorService utilizatorService, ParticipantService participantService, ProbaService probaService, InscriereService inscriereService, List<Participant> participanti) {
        this.utilizatorService = utilizatorService;
        this.participantService = participantService;
        this.probaService = probaService;
        this.inscriereService = inscriereService;

        ObservableList<Participant> observableParticipants = FXCollections.observableArrayList(participanti);
        participantiTableView.setItems(observableParticipants);
    }

    @FXML
    public void initialize() {
        idParticipantColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        varstaColumn.setCellValueFactory(new PropertyValueFactory<>("varsta"));
        nrProbeColumn.setCellValueFactory(cellData -> {
            Participant participant = cellData.getValue();
            int nrProbe = inscriereService.getNrProbeParticipant(participant.getId());
            return new SimpleIntegerProperty(nrProbe).asObject();
        });
    }
}