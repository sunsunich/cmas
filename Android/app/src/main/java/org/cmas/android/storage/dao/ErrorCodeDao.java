package org.cmas.android.storage.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import org.cmas.android.storage.entities.i18n.ErrorCode;

import java.util.List;

@Dao
public abstract class ErrorCodeDao {

    @Delete
    public abstract void delete(ErrorCode entity);

    @Update
    public abstract void update(ErrorCode entity);

    @Insert
    public abstract void insert(ErrorCode entity);

    @Query("SELECT * FROM ERROR_CODE WHERE CODE = :key")
    public abstract ErrorCode getByKey(String key);

    @Query("SELECT * FROM ERROR_CODE")
    public abstract List<ErrorCode> getAll();

    public String saveOrUpdate(ErrorCode entity) {
        if (getByKey(entity.code) == null) {
            insert(entity);
        } else {
            update(entity);
        }
        return entity.code;
    }
}
