package org.cmas.util.dao;

import org.cmas.presentation.dao.DAOException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.security.SecureRandom;

public class IdGeneratingDaoImpl<T> extends HibernateDaoImpl<T> implements IdGeneratingDao<T> {

    protected final SecureRandom rnd = new SecureRandom();

    @Transactional
    @Override
    public boolean isIdUnique(long id) {
        Criteria criteria = createCriteria().add(Restrictions.eq("id", id));
        criteria.setProjection(Projections.rowCount());
        Object result = criteria.uniqueResult();
        return result == null || ((Number) result).intValue() == 0;
    }

    @Override
    public long generateId() {
        long id;
        do {
            id = Math.abs(rnd.nextLong());
        } while (!isIdUnique(id));
        return id;
    }

    @Override
    public <E> E createNew(Class<E> cl) throws DAOException {
        long id = generateId();
        try {
            Constructor<E> constructor = cl.getConstructor(Long.TYPE);
            return constructor.newInstance(id);
        } catch (Exception e) {
            throw new DAOException("Error initiating object of class: " + cl.getName(), e);
        }
    }
}
