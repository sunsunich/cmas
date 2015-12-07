package org.cmas.dao;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.entities.HasId;

public interface GeneralDao<E extends HasId> extends CreateTableDao{

    E cursorToEntity(Cursor cursor, int index);

    String[] getAllColumns();

    String getTableName();

    long save(SQLiteDatabase database, E entity);

    int update(SQLiteDatabase database, E entity);

    void saveOrUpdate(SQLiteDatabase database, E entity);

    E getById(SQLiteDatabase database, long id);

    void delete(SQLiteDatabase database, E entity);
}
