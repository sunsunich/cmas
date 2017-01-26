package org.cmas.util.dao;

import org.cmas.presentation.model.SortDirection;
import org.cmas.presentation.model.SortLimitInfo;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.proxy.HibernateProxy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;


@SuppressWarnings({"unchecked"})
@Transactional
public class HibernateDaoImpl<T> implements HibernateDao<T> {
    private SessionFactory sessionFactory;
    private final Class<T> modelClass;

    public static <T> T initializeAndUnproxy(T entity) {
        if (entity == null) {
            throw new NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
                    .getImplementation();
        }
        return entity;
    }

    public HibernateDaoImpl() {
        modelClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public HibernateDaoImpl(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    protected boolean isCommonFieldUnique(Long id, String field, Object value) {
        if (value == null || value instanceof String && ((CharSequence) value).length() == 0) {
            return true;
        }
        Criteria crit = getSession().createCriteria(modelClass);
        crit.add(Restrictions.eq(field, value));
        if (id != null) {
            crit.add(Restrictions.ne("id", id));
        }
        return crit.uniqueResult() == null;
    }

    @Required
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Class<T> getModelClass() {
        return modelClass;
    }

    @Override
    public Criteria createCriteria() {
        Session sess = sessionFactory.getCurrentSession();
        return sess.createCriteria(getModelClass());
    }

    @Override
    public Query createQuery(String hql) {
        Session sess = sessionFactory.getCurrentSession();
        return sess.createQuery(hql);
    }

    @Override
    public SQLQuery createSQLQuery(String sql) {
        Session sess = sessionFactory.getCurrentSession();
        return sess.createSQLQuery(sql);
    }

    public List<T> findAll() {
        Criteria criteria = createCriteria();
        return (List<T>) criteria.list();
    }

    @Override
    public List<T> list(Query query) {
        //noinspection unchecked
        return (List<T>) Collections.checkedList(query.list(), modelClass);
    }

    @NotNull
    protected T loadModel(@NotNull Serializable id) {
        Session sess = getSession();
        return (T) sess.load(getModelClass(), id);
    }

    @Nullable
    @Transactional
    @Override
    public T getModel(@NotNull Serializable id) {
        Session sess = getSession();
        return (T) sess.get(getModelClass(), id);
    }

    @Override
    public T mergeModel(@NotNull T model) {
        Session sess = getSession();
        return (T) sess.merge(model);
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Serializable save(@NotNull T model) {
        Session sess = getSession();
        Serializable id = sess.save(model);
        sess.flush();
        return id;
    }

    protected boolean isFieldUnique(Long id, String field, Object value) {
        if (value == null || value instanceof String && ((CharSequence) value).length() == 0) {
            return true;
        }
        Criteria crit = createCriteria();
        crit.add(Restrictions.eq(field, value));
        if (id != null) {
            crit.add(Restrictions.ne("id", id));
        }
        return crit.uniqueResult() == null;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void saveModel(@NotNull T model) {
        Session sess = getSession();
        sess.save(model);
        sess.flush();
    }

    @Transactional
    @Override
    public void updateModel(@NotNull T model) {
        Session sess = getSession();
        sess.update(model);
    }

    @Transactional
    @Override
    public void deleteModel(@NotNull T model) {
        Session sess = getSession();
        sess.delete(model);
    }

    @NotNull
    @Transactional
    @Override
    public T getById(@NotNull Serializable id) {
        return loadModel(id);
    }

    @Override
    public T findById(@NotNull Serializable id) {
        Criteria crit = createCriteria();
        crit.add(Restrictions.eq("id", id));
        return (T) crit.uniqueResult();
    }

    @Override
    public Serializable findMaxId() {
        // что он выдаст для нуля элементов?
        List list = createCriteria().setProjection(Projections.max("id")).list();
        return (Serializable) list.get(0);
    }

    protected void addSortLimit(Criteria criteria, SortLimitInfo data) {
        if (data.getDir() != null && data.getSort() != null) {
            if (data.getDir() == SortDirection.DESC) {
                criteria.addOrder(Order.desc(data.getSort()));
            } else {
                criteria.addOrder(Order.asc(data.getSort()));
            }
        }
        if (data.getLimit() > 0) {
            criteria.setMaxResults(data.getLimit());
        }
        if (data.getStart() > 0) {
            criteria.setFirstResult(data.getStart());
        }
    }
}

