//package mpp.domain;
//
//public class Utilizator extends Entity<Integer> {
//    private String numeUtilizator;
//    private String parola;
//
//    public Utilizator(Integer id, String numeUtilizator, String parola) {
//        super(id);
//        this.numeUtilizator = numeUtilizator;
//        this.parola = parola;
//    }
//
//    public Utilizator(String numeUtilizator, String parola) {
//        this.numeUtilizator = numeUtilizator;
//        this.parola = parola;
//    }
//
//    public Utilizator() {
//
//    }
//
//    public String getNumeUtilizator() {
//        return numeUtilizator;
//    }
//
//    public void setNumeUtilizator(String numeUtilizator) {
//        this.numeUtilizator = numeUtilizator;
//    }
//
//    public String getParola() {
//        return parola;
//    }
//
//    public void setParola(String parola) {
//        this.parola = parola;
//    }
//}


package mpp.domain;

import jakarta.persistence.*;

@jakarta.persistence.Entity
@Table(name = "utilizatori")
public class Utilizator extends EntityHibernate<Integer>  {
    private String numeUtilizator;
    private String parola;



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


    public Utilizator() {
        super();
    }

    public Utilizator(Integer id, String numeUtilizator, String parola) {
        super(id);
        this.numeUtilizator = numeUtilizator;
        this.parola = parola;
    }

    public Utilizator(String numeUtilizator, String parola) {
        this.numeUtilizator = numeUtilizator;
        this.parola = parola;
    }

    @Column(name = "numeUtilizator")
    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public void setNumeUtilizator(String numeUtilizator) {
        this.numeUtilizator = numeUtilizator;
    }

    @Column(name = "parola")
    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }
}