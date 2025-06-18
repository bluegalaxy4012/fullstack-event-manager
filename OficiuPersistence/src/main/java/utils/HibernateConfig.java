package utils;

import mpp.domain.Inscriere;
import mpp.domain.Participant;
import mpp.domain.Proba;
import mpp.domain.Utilizator;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HibernateConfig {

    private static final Logger logger = LogManager.getLogger(HibernateConfig.class);
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateConfig.class) {
                if (sessionFactory == null) {
                    sessionFactory = new Configuration()
                            .configure()
                            .addAnnotatedClass(Participant.class)
                            .addAnnotatedClass(Utilizator.class)
                            .addAnnotatedClass(Proba.class)
                            .addAnnotatedClass(Inscriere.class)
                            .buildSessionFactory();
                    logger.info("Hibernate SessionFactory initialized successfully.");
                }
            }
        }
        return sessionFactory;
    }
}