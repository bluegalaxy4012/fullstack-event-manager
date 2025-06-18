package mpp.domain;

public class Proba extends Entity<Integer> {
    private String distanta;
    private String stil;

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
}