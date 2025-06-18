package mpp.dto;

import java.io.Serializable;

public class UtilizatorDTO extends EntityDTO {
    private String numeUtilizator;
    private String parola;

    public UtilizatorDTO(int id, String numeUtilizator, String parola) {
        this.setId(id);
        this.numeUtilizator = numeUtilizator;
        this.parola = parola;
    }

    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public void setNumeUtilizator(String numeUtilizator) {
        this.numeUtilizator = numeUtilizator;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        return "UtilizatorDTO[id=" + getId() + ", numeUtilizator=" + numeUtilizator + ", parola=" + parola + "]";
    }
}