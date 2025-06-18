package mpp.jsonprotocol;

import mpp.dto.*;

import java.util.Arrays;

public class Response {
    private ResponseType type;
    private String errorMessage;
    private UtilizatorDTO utilizator;
    private ParticipantDTO participant;
    private ProbaDTO proba;
    private InscriereDTO inscriere;
    private ProbaDTO[] probe;
    private ParticipantDTO[] participanti;
    private int count;

    public Response() {}

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public UtilizatorDTO getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(UtilizatorDTO utilizator) {
        this.utilizator = utilizator;
    }

    public ParticipantDTO getParticipant() {
        return participant;
    }

    public void setParticipant(ParticipantDTO participant) {
        this.participant = participant;
    }

    public ProbaDTO getProba() {
        return proba;
    }

    public void setProba(ProbaDTO proba) {
        this.proba = proba;
    }

    public InscriereDTO getInscriere() {
        return inscriere;
    }

    public void setInscriere(InscriereDTO inscriere) {
        this.inscriere = inscriere;
    }

    public ProbaDTO[] getProbe() {
        return probe;
    }

    public void setProbe(ProbaDTO[] probe) {
        this.probe = probe;
    }

    public ParticipantDTO[] getParticipanti() {
        return participanti;
    }

    public void setParticipanti(ParticipantDTO[] participanti) {
        this.participanti = participanti;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", errorMessage='" + errorMessage + '\'' +
                ", utilizator=" + utilizator +
                ", participant=" + participant +
                ", proba=" + proba +
                ", inscriere=" + inscriere +
                ", probe=" + Arrays.toString(probe) +
                ", participanti=" + Arrays.toString(participanti) +
                ", count=" + count +
                '}';
    }
}