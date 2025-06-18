package mpp.domain;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class EntityHibernate<ID> {
    private ID id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public EntityHibernate() {
    }

    public EntityHibernate(ID id) {
        this.id = id;
    }
}