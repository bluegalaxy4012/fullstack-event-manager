package mpp.repository;

import mpp.domain.Utilizator;

public interface UtilizatorHibernateRepository extends RepositoryHibernate<Integer, Utilizator> {
    Utilizator findUtilizator(String numeUtilizator, String parola);
}