package mpp.jsonprotocol;

import mpp.dto.*;

public class Request {
    private RequestType type;
    private UtilizatorDTO utilizator;
    private ParticipantDTO participant;
    private ProbaDTO proba;
    private InscriereDTO inscriere;

    public Request() {}

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", utilizator=" + utilizator +
                ", participant=" + participant +
                ", proba=" + proba +
                ", inscriere=" + inscriere +
                '}';
    }
}