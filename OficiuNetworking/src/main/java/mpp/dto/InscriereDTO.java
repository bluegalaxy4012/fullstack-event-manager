package mpp.dto;

import java.io.Serializable;

public class InscriereDTO extends EntityDTO {
    private int participantId;
    private int probaId;

    public InscriereDTO(int id, int participantId, int probaId) {
        this.setId(id);
        this.participantId = participantId;
        this.probaId = probaId;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public int getProbaId() {
        return probaId;
    }

    public void setProbaId(int probaId) {
        this.probaId = probaId;
    }

    @Override
    public String toString() {
        return "InscriereDTO[id=" + getId() + ", participantId=" + participantId + ", probaId=" + probaId + "]";
    }
}