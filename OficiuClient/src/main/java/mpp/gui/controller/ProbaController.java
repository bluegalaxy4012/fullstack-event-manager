package mpp.gui.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import mpp.domain.Participant;
import mpp.domain.Proba;
import mpp.service.OficiuException;
import mpp.service.OficiuServices;

import java.util.List;
import java.util.stream.StreamSupport;

public class ProbaController {
    @FXML
    private TableView<Participant> participantiTableView;
    @FXML
    private TableColumn<Participant, String> idParticipantColumn;
    @FXML
    private TableColumn<Participant, Integer> varstaColumn;
    @FXML
    private TableColumn<Participant, Integer> nrProbeColumn;

    private OficiuServices server;
    private Proba selectedProba;



    public void setData(OficiuServices server, Proba proba) throws OficiuException {
        this.server = server;
        selectedProba = proba;

        loadData();
    }

    public Proba getSelectedProba() {
        return selectedProba;
    }

    @FXML
    public void initialize() {
        idParticipantColumn.setCellValueFactory(new PropertyValueFactory<>("nume"));
        varstaColumn.setCellValueFactory(new PropertyValueFactory<>("varsta"));
        nrProbeColumn.setCellValueFactory(cellData -> {
            Participant participant = cellData.getValue();
            int nrProbe = 0;
            try {
                nrProbe = server.getNrProbeParticipant(participant.getId());
            } catch (OficiuException e) {
                throw new RuntimeException(e);
            }
            return new SimpleIntegerProperty(nrProbe).asObject();
        });
    }

    public void loadData() throws OficiuException {
        List<Participant> participanti = StreamSupport.stream(server.findParticipantiByProba(selectedProba.getId()).spliterator(), false).toList();
        ObservableList<Participant> observableParticipants = FXCollections.observableArrayList(participanti);
        participantiTableView.setItems(observableParticipants);
    }
}