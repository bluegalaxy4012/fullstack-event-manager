//package mpp.domain;
//
//public class Inscriere extends Entity<Integer> {
//    private Participant participant;
//    private Proba proba;
//
//    public Inscriere(Participant participant, Proba proba) {
//        this.participant = participant;
//        this.proba = proba;
//    }
//
//    public Inscriere() {
//
//    }
//
//
//    public Participant getParticipant() {
//        return participant;
//    }
//
//    public void setParticipant(Participant participant) {
//        this.participant = participant;
//    }
//
//    public Proba getProba() {
//        return proba;
//    }
//
//    public void setProba(Proba proba) {
//        this.proba = proba;
//    }
//}



package mpp.domain;

import jakarta.persistence.*;

@jakarta.persistence.Entity
@Table(name = "inscrieri")
public class Inscriere extends EntityHibernate<Integer> {
    private Participant participant;
    private Proba proba;


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



    public Inscriere() {
        super();
    }

    public Inscriere(Participant participant, Proba proba) {
        this.participant = participant;
        this.proba = proba;
    }

    @ManyToOne
    @JoinColumn(name = "participant_id")
    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    @ManyToOne
    @JoinColumn(name = "proba_id")
    public Proba getProba() {
        return proba;
    }

    public void setProba(Proba proba) {
        this.proba = proba;
    }
}