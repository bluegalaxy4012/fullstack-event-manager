package mpp.repository;

import mpp.domain.EntityHibernate;
import java.util.Optional;

public interface RepositoryHibernate<ID, E extends EntityHibernate<ID>> {
    Optional<E> findOne(ID id);
    Iterable<E> findAll();
    Optional<E> save(E entity);
    Optional<E> delete(ID id);
    Optional<E> update(E entity);
}