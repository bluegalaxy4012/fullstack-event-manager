//package mpp.domain;
//
//public class Proba extends Entity<Integer> {
//    private String distanta;
//    private String stil;
//
//
//    public Proba(Integer id, String distanta, String stil) {
//        super(id);
//        this.distanta = distanta;
//        this.stil = stil;
//    }
//
//    public Proba(String distanta, String stil) {
//        this.distanta = distanta;
//        this.stil = stil;
//    }
//
//    public Proba() {
//
//    }
//
//    public String getDistanta() {
//        return distanta;
//    }
//
//    public void setDistanta(String distanta) {
//        this.distanta = distanta;
//    }
//
//    public String getStil() {
//        return stil;
//    }
//
//    public void setStil(String stil) {
//        this.stil = stil;
//    }
//}





package mpp.domain;

import jakarta.persistence.*;

@jakarta.persistence.Entity
@Table(name = "probe")
public class Proba extends EntityHibernate<Integer> {
    private String distanta;
    private String stil;


    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setId(Integer id) {
        super.setId(id);
    }

    public Proba() {
        super();
    }

    public Proba(Integer id, String distanta, String stil) {
        super(id);
        this.distanta = distanta;
        this.stil = stil;
    }

    public Proba(String distanta, String stil) {
        this.distanta = distanta;
        this.stil = stil;
    }

    @Column(name = "distanta")
    public String getDistanta() {
        return distanta;
    }

    public void setDistanta(String distanta) {
        this.distanta = distanta;
    }

    @Column(name = "stil")
    public String getStil() {
        return stil;
    }

    public void setStil(String stil) {
        this.stil = stil;
    }



    @Override
    public String toString() {
        return "Proba{" +
                "id=" + getId() +
                ", distanta='" + distanta + '\'' +
                ", stil='" + stil + '\'' +
                '}';
    }
}