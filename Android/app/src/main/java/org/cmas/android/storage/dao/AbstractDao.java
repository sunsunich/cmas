package org.cmas.android.storage.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import org.cmas.android.storage.entities.IdEntity;

@Dao
public abstract class AbstractDao<T extends IdEntity> {

    @Delete
    public abstract void delete(T entity);

    @Update
    public abstract void update(T entity);

    @Insert
    public abstract long insert(T entity);

    public abstract T getById(long id);

    public long saveOrUpdate(T entity) {
        if (entity.id == 0 || getById(entity.id) == null) {
            return insert(entity);
        } else {
            update(entity);
            return entity.id;
        }
    }
}
