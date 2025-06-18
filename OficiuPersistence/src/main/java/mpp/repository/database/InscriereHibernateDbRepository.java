package mpp.repository.database;

import mpp.domain.Inscriere;
import mpp.domain.Participant;
import mpp.domain.Proba;
import mpp.repository.InscriereHibernateRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateConfig;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class InscriereHibernateDbRepository implements InscriereHibernateRepository {
    private final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();

//    public InscriereHibernateDbRepository(Properties props) {
//        this.sessionFactory = HibernateConfig.getSessionFactory(props);
//    }

//    void updateSession()
//    {
//        sessionFactory = HibernateConfig.getSessionFactory();
//    }

    @Override
    public Participant findParticipantById(int id) {

        try (Session session = sessionFactory.openSession()) {
            return session.get(Participant.class, id);
        }
    }

    @Override
    public Proba findProbaById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Proba.class, id);
        }
    }

    @Override
    public Iterable<Participant> findParticipantiByProba(int idProba) {
        try (Session session = sessionFactory.openSession()) {
            Query<Participant> query = session.createQuery(
                    "SELECT i.participant FROM Inscriere i WHERE i.proba.id = :probaId",
                    Participant.class
            );
            query.setParameter("probaId", idProba);
            return query.list();
        }
    }

    @Override
    public int getNrParticipantiInscrisi(int idProba) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(i) FROM Inscriere i WHERE i.proba.id = :probaId",
                    Long.class
            );
            query.setParameter("probaId", idProba);
            return query.uniqueResult().intValue();
        }
    }

    @Override
    public int getNrProbeParticipant(int idParticipant) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(i) FROM Inscriere i WHERE i.participant.id = :participantId",
                    Long.class
            );
            query.setParameter("participantId", idParticipant);
            return query.uniqueResult().intValue();
        }
    }

    @Override
    public Optional<Inscriere> findOne(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Inscriere.class, id));
        }
    }

    @Override
    public Iterable<Inscriere> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Inscriere", Inscriere.class).list();
        }
    }

    @Override
    public Optional<Inscriere> save(Inscriere entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            entity.setId(null);
            session.persist(entity);
            tx.commit();
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Inscriere> delete(Integer id) {
        Optional<Inscriere> inscriere = findOne(id);
        inscriere.ifPresent(i -> {
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                session.remove(i);
                tx.commit();
            }
        });
        return inscriere;
    }

    @Override
    public Optional<Inscriere> update(Inscriere entity) {
        try (Session session = sessionFactory.openSession()) {
            Inscriere existing = session.get(Inscriere.class, entity.getId());
            if (existing == null) {
                return Optional.empty();
            }
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return Optional.of(entity);
        }
    }

}