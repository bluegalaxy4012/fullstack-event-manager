package mpp.repository.database;

import mpp.domain.Proba;
import mpp.repository.ProbaHibernateRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utils.HibernateConfig;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Properties;


@Repository
public class ProbaHibernateDbRepository implements ProbaHibernateRepository {
//    private final SessionFactory sessionFactory;
//
//    public ProbaHibernateDbRepository() {
//        this.sessionFactory = HibernateConfig.getSessionFactory();
//    }
//
//    public ProbaHibernateDbRepository(Properties props) {
//        this.sessionFactory = HibernateConfig.getSessionFactory(props);
//    }

    private final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();

    @Override
    public Optional<Proba> findOne(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Proba.class, id));
        }
    }

    @Override
    public Iterable<Proba> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Proba", Proba.class).list();
        }
    }

    @Override
    public Optional<Proba> save(Proba entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            entity.setId(null);
            session.persist(entity);
            tx.commit();
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Proba> delete(Integer id) {
        Optional<Proba> proba = findOne(id);
        proba.ifPresent(p -> {
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                session.remove(p);
                tx.commit();
            }
        });
        return proba;
    }

    @Override
    public Optional<Proba> update(Proba entity) {
        try (Session session = sessionFactory.openSession()) {
            Proba existing = session.get(Proba.class, entity.getId());
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