package mpp.repository.database;

import mpp.domain.Utilizator;
import mpp.repository.UtilizatorHibernateRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utils.HibernateConfig;

import java.util.Optional;
import java.util.Properties;

public class UtilizatorHibernateDbRepository implements UtilizatorHibernateRepository {
    private final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();

//    public UtilizatorHibernateDbRepository(Properties props) {
//        this.sessionFactory = HibernateConfig.getSessionFactory(props);
//    }

    @Override
    public Optional<Utilizator> findOne(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Utilizator.class, id));
        }
    }

    @Override
    public Iterable<Utilizator> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Utilizator", Utilizator.class).list();
        }
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            entity.setId(null);
            session.persist(entity);
            tx.commit();
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Utilizator> delete(Integer id) {
        Optional<Utilizator> utilizator = findOne(id);
        utilizator.ifPresent(u -> {
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                session.remove(u);
                tx.commit();
            }
        });
        return utilizator;
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        try (Session session = sessionFactory.openSession()) {
            Utilizator existing = session.get(Utilizator.class, entity.getId());
            if (existing == null) {
                return Optional.empty();
            }
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return Optional.of(entity);
        }
    }


    @Override
    public Utilizator findUtilizator(String numeUtilizator, String parola) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Utilizator where numeUtilizator = :nume and parola = :parola", Utilizator.class)
                    .setParameter("nume", numeUtilizator)
                    .setParameter("parola", parola)
                    .uniqueResult();
        }
    }
}