package org.cmas.dao.dictionary;

import android.content.ContentValues;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.dao.GeneralDaoImpl;
import org.cmas.entities.DictionaryEntity;

public abstract class DictionaryDataDaoImpl<T extends DictionaryEntity> extends GeneralDaoImpl<T>
        implements DictionaryDataDao<T> {

    public static final String COLUMN_NAME = "name";
    protected static final String COLUMN_DELETED = "deleted";

    // Database creation SQL statement
    private static final String CREATE_TABLE_QUERY =
            " ( "
                    + COLUMN_ID + " integer primary key, "
                    + COLUMN_VERSION + " integer not null, "
                    + COLUMN_DELETED + " integer not null default 0, "
                    + COLUMN_NAME + " text not null ";

    private static final String[] ALL_COLUMNS = {
            COLUMN_ID, COLUMN_VERSION, COLUMN_DELETED, COLUMN_NAME
    };


    protected abstract String getTableCreateQueryEnding();

    protected abstract T constructEntity();

    @Override
    public String[] getAllColumns() {
        return ALL_COLUMNS;
    }

    @Override
    public String getAllColumnsStr() {
        String[] allColumns = getAllColumns();
        String tableAlias = getTableAlias();
        StringBuilder stringBuilder = new StringBuilder(tableAlias).append('.').append(allColumns[0]);
        for (int i = 1; i < allColumns.length; i++) {
            stringBuilder.append(',')
                    .append(tableAlias)
                    .append('.')
                    .append(allColumns[i]);
        }

        return stringBuilder.toString();
    }

    @Override
    protected String getDataBaseCreateQuery() {
        return "create table " + getTableName() + CREATE_TABLE_QUERY + getTableCreateQueryEnding();
    }

    @Override
    public T cursorToEntity(Cursor cursor, int index) {
        T entity = constructEntity();
        int i = index;
        entity.setId(cursor.getLong(i));
        i++;
        entity.setVersion(cursor.getLong(i));
        i++;
        entity.setDeleted(cursor.getInt(i) != 0);
        i++;
        entity.setName(cursor.getString(i));
        return entity;
    }

    @Override
    protected ContentValues entityToContentValues(T entity) {
        ContentValues values = super.entityToContentValues(entity);
        values.put(COLUMN_VERSION, entity.getVersion());
        values.put(COLUMN_DELETED, entity.isDeleted());
        String name = entity.getName();
        values.put(COLUMN_NAME, name == null ? "" : name);
        return values;
    }

    @Override
    public long getMaxVersion(SQLiteDatabase database) {
        String getMaxVersionQuery =
                "select max(" + COLUMN_VERSION + ") from " + getTableName();
        Cursor cursor = database.rawQuery(
                getMaxVersionQuery, null
        );
        try {
            if (cursor.moveToFirst()) {
                return cursor.getLong(0);
            }
            return 0L;
        } finally {
            cursor.close();
        }
    }

    @Override
    public void delete(SQLiteDatabase database, T entity) {
        long id = entity.getId();
        T entityFromDB = getById(database, id);
        if (entityFromDB == null) {
            entity.setDeleted(true);
            save(database, entity);
        } else {
            entityFromDB.setDeleted(true);
            update(database, entityFromDB);
        }
    }
}

