package mpp.dto;

import java.io.Serializable;

public class ParticipantDTO extends EntityDTO {
    private String nume;
    private int varsta;

    public ParticipantDTO(int id, String nume, int varsta) {
        this.setId(id);
        this.nume = nume;
        this.varsta = varsta;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    @Override
    public String toString() {
        return "ParticipantDTO[id=" + getId() + ", nume=" + nume + ", varsta=" + varsta + "]";
    }
}