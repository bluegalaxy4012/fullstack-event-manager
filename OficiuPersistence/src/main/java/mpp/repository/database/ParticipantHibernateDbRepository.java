package mpp.repository.database;

import mpp.domain.Participant;
import mpp.repository.ParticipantHibernateRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utils.HibernateConfig;

import java.util.Optional;
import java.util.Properties;

public class ParticipantHibernateDbRepository implements ParticipantHibernateRepository {
    private final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();

//    public ParticipantHibernateDbRepository(Properties props) {
//        this.sessionFactory = HibernateConfig.getSessionFactory(props);
//    }

    @Override
    public Optional<Participant> findOne(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Participant.class, id));
        }
    }

    @Override
    public Iterable<Participant> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Participant", Participant.class).list();
        }
    }

    @Override
    public Optional<Participant> save(Participant entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            entity.setId(null);
            session.persist(entity);
            tx.commit();
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Participant> delete(Integer id) {
        Optional<Participant> participant = findOne(id);
        participant.ifPresent(p -> {
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                session.remove(p);
                tx.commit();
            }
        });
        return participant;
    }

    @Override
    public Optional<Participant> update(Participant entity) {
        try (Session session = sessionFactory.openSession()) {
            Participant existing = session.get(Participant.class, entity.getId());
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