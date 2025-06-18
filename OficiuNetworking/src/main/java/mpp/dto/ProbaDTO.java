package mpp.dto;

import java.io.Serializable;

public class ProbaDTO extends EntityDTO {
    private String distanta;
    private String stil;

    public ProbaDTO(int id, String distanta, String stil) {
        this.setId(id);
        this.distanta = distanta;
        this.stil = stil;
    }

    public String getDistanta() {
        return distanta;
    }

    public void setDistanta(String distanta) {
        this.distanta = distanta;
    }

    public String getStil() {
        return stil;
    }

    public void setStil(String stil) {
        this.stil = stil;
    }

    @Override
    public String toString() {
        return "ProbaDTO[id=" + getId() + ", distanta=" + distanta + ", stil=" + stil + "]";
    }
}