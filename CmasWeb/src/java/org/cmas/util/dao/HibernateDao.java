package org.cmas.util.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

public interface HibernateDao<T> extends AbstractDao<T, Serializable> {
    
    void saveModel(@NotNull T object);

	Serializable save(@NotNull T model);

    void updateModel(@NotNull T object);

    void deleteModel(@NotNull T object);

    T mergeModel(@NotNull T model);

    /**
     * session.get
     * @param id
     * @return null if entry with given id is not found
     */
    @Nullable
    T getModel(@NotNull Serializable id);
    /**
     * session.load - lazy version, throws exception if input is invalid
     *
     * @param id
     * @return
     */
    T getById(@NotNull Serializable id);
    /**
     * criteria.uniqResult - less lazy version.
     * @param id
     * @return
     */
    T findById(@NotNull Serializable id);
    
    //List<T> findAll();

    /**
     * Checked variant of query.list()
     */
    List<T> list(Query query);

    Serializable findMaxId();

    Criteria createCriteria();

    Query createQuery(String hql);

    SQLQuery createSQLQuery(String sql);
}
