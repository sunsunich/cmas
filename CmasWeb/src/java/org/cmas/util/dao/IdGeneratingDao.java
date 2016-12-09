package org.cmas.util.dao;

import org.cmas.presentation.dao.DAOException;

public interface IdGeneratingDao<T> extends HibernateDao<T>{

    boolean isIdUnique(long id);

    long generateId();

    <E> E createNew(Class<E> cl) throws DAOException;
}
