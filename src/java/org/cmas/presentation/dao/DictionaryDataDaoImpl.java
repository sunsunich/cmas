package org.cmas.presentation.dao;

import org.cmas.entities.DictionaryEntity;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class DictionaryDataDaoImpl<T extends DictionaryEntity> extends HibernateDaoImpl<T> implements DictionaryDataDao<T> {

    @Nullable
    @Override
    public T getByName(String name) {
        //noinspection unchecked
        return (T) createCriteria()
                .add(Restrictions.eq("name", name))
                .add(Restrictions.eq("deleted", false))
                .uniqueResult();
    }

    @Override
    public List<T> getAllOlderThanVersion(long version) {
        //noinspection unchecked
        return createCriteria()
                .add(Restrictions.gt("version", version))
                        //  .add(Restrictions.eq("deleted", false))
                .list();
    }

    @Override
    public Long getMaxVersion() {
        Object result = createCriteria().setProjection(Projections.max("version")).uniqueResult();
        return result == null ? 0L : (Long) result;

    }

    //TODO local delete all fields except id, version, deleted - not to request deleted from server
}
