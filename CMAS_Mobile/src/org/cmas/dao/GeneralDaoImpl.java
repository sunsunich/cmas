package org.cmas.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.entities.HasId;

public abstract class GeneralDaoImpl<E extends HasId> implements GeneralDao<E> {

    public static final String COLUMN_ID = "_id";
    protected static final String COLUMN_VERSION = "version";

    @Override
    public long save(SQLiteDatabase database, E entity) {
        ContentValues values = entityToContentValues(entity);
        return database.insert(getTableName(), null, values);

    }

    @Override
    public int update(SQLiteDatabase database, E entity) {
        ContentValues values = entityToContentValues(entity);
        return database.update(getTableName(), values, COLUMN_ID + " = " + entity.getId(), null);
    }

    @Override
    public void saveOrUpdate(SQLiteDatabase database, E entity) {
        E entityFromDB = getById(database, entity.getId());
        if(entityFromDB == null){
            save(database, entity);
        }
        else{
            update(database, entity);
        }
    }

    @Override
    public E getById(SQLiteDatabase database, long id) {
        Cursor cursor = database.query(
                getTableName()
                , getAllColumns(), COLUMN_ID + " = " + id
                , null, null, null, null
        );
        try {
            if (cursor.moveToFirst()) {
                return cursorToEntity(cursor, 0);
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    @Override
    public void delete(SQLiteDatabase database, E entity) {
        database.delete(getTableName(), COLUMN_ID + " = " + entity.getId(), null);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(getDataBaseCreateQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        Log.w(GeneralDaoImpl.class.getName()
                , "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + getTableName());
        onCreate(database);
    }

    protected ContentValues entityToContentValues(E entity){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, entity.getId());
        return values;
    }

    protected abstract String getDataBaseCreateQuery();

}
