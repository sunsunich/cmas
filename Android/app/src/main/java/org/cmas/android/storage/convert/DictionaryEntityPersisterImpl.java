package org.cmas.android.storage.convert;


import org.cmas.android.storage.dao.DictionaryEntityDao;
import org.cmas.android.storage.entities.DictionaryEntity;
import org.cmas.service.VersionableEntityPersister;

import javax.annotation.Nullable;

public abstract class DictionaryEntityPersisterImpl<D extends DictionaryEntityDao<S>, C extends org.cmas.entities.DictionaryEntity, S extends DictionaryEntity>
        extends EntityPersisterImpl<D, C, S> implements VersionableEntityPersister<C> {

    protected ModelConverter<C, S> modelConverter = new ModelMapperConverterImpl<>();

    protected abstract Class<S> getEntityClass();

    @Override
    public long getMaxVersion() {
        return getDao().getMaxVersion();
    }

    @Override
    public void persist(@Nullable C container) {
        persistEntity(modelConverter.convertFromSource(getEntityClass(), container));
    }

    protected void persistEntity(@Nullable S entity) {
        if (entity == null) {
            return;
        }
        D dao = getDao();
        if (entity.deleted) {
            dao.delete(entity);
        } else {
            dao.saveOrUpdate(entity);
        }
    }
}
