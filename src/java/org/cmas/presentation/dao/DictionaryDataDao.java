package org.cmas.presentation.dao;

import org.cmas.entities.DictionaryEntity;
import org.cmas.util.dao.HibernateDao;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DictionaryDataDao<T extends DictionaryEntity> extends HibernateDao<T> {

    @Nullable
    T getByName(String name);

    List<T> getAllOlderThanVersion(long version);

    Long getMaxVersion();
}
