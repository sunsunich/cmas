package org.cmas.android.storage.dao;

import androidx.room.Dao;
import org.cmas.android.storage.entities.DictionaryEntity;

@Dao
public abstract class DictionaryEntityDao<T extends DictionaryEntity> extends AbstractDao<T> {

    public abstract long getMaxVersion();
}
