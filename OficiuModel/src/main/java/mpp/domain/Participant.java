//package mpp.domain;
//
//
//
//public class Participant extends Entity<Integer> {
//    private String nume;
//    private int varsta;
//
//
//    public Participant(Integer id, String nume, int varsta) {
//        super(id);
//        this.nume = nume;
//        this.varsta = varsta;
//    }
//
//    public Participant(String nume, int varsta) {
//        this.nume = nume;
//        this.varsta = varsta;
//    }
//
//    public Participant() {
//
//    }
//
//
//    public String getNume() {
//        return nume;
//    }
//
//    public void setNume(String nume) {
//        this.nume = nume;
//    }
//
//    public int getVarsta() {
//        return varsta;
//    }
//
//    public void setVarsta(int varsta) {
//        this.varsta = varsta;
//    }
//
//}




package mpp.domain;

import jakarta.persistence.*;

@jakarta.persistence.Entity
@Table(name = "participanti")
public class Participant extends EntityHibernate<Integer> {
    private String nume;
    private int varsta;


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


    public Participant() {
        super();
    }

    public Participant(Integer id, String nume, int varsta) {
        super(id);
        this.nume = nume;
        this.varsta = varsta;
    }

    public Participant(String nume, int varsta) {
        this.nume = nume;
        this.varsta = varsta;
    }

    @Column(name = "nume")
    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Column(name = "varsta")
    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }
}