package org.cmas.android.storage.convert;


import org.cmas.android.storage.dao.AbstractDao;
import org.cmas.android.storage.entities.IdEntity;
import org.cmas.entities.HasId;
import org.cmas.service.EntityPersister;

import javax.annotation.Nullable;
import java.util.Collection;

public abstract class EntityPersisterImpl<D extends AbstractDao<S>, C extends HasId, S extends IdEntity>
        implements EntityPersister<C> {

    protected abstract D getDao();

    @Override
    public void persist(@Nullable Collection<C> containerCollection) {
        if (containerCollection == null) {
            return;
        }
        for (C container : containerCollection) {
            persist(container);
        }
    }
}
