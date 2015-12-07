package org.cmas.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.Globals;
import org.cmas.entities.User;

import java.text.ParseException;
import java.util.Date;

public abstract class UserDaoImpl<T extends User> extends GeneralDaoImpl<T> implements UserDao<T> {

    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_DOB = "dob";

    // Database creation SQL statement
    private static final String CREATE_TABLE_QUERY =
            "( "
                    + COLUMN_ID + " integer primary key, "
                    + COLUMN_EMAIL + " text not null, "
                    + COLUMN_PASSWORD + " text not null, "
                    + COLUMN_CODE + " text, "
                    + COLUMN_DOB + " text "
            ;

    private static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_EMAIL,
            COLUMN_PASSWORD,
            COLUMN_CODE,
            COLUMN_DOB,

    };

    @Override
    protected String getDataBaseCreateQuery() {
        return "create table " + getTableName() + CREATE_TABLE_QUERY + getTableCreateQueryEnding();
    }

    @Override
    public String[] getAllColumns() {
        return ALL_COLUMNS;
    }

    protected abstract String getTableCreateQueryEnding();

    protected abstract T constructEntity();

    @Override
    public T cursorToEntity(Cursor cursor, int index) {
        T entity = constructEntity();
        int i = index;
        entity.setId(cursor.getLong(i));
        i++;
        entity.setEmail(cursor.getString(i));
        i++;
        entity.setPassword(cursor.getString(i));
        i++;
        entity.setMobileLockCode(cursor.getString(i));
        i++;
        String dob = cursor.getString(i);
        if (dob != null) {
            try {
                entity.setDob(Globals.getDTF().parse(dob));
            } catch (ParseException ignored) {
            }
        }
        return entity;
    }

    @Override
    protected ContentValues entityToContentValues(T entity) {
        ContentValues values = super.entityToContentValues(entity);
        values.put(COLUMN_EMAIL, entity.getEmail());
        values.put(COLUMN_PASSWORD, entity.getPassword());
        values.put(COLUMN_CODE, entity.getMobileLockCode());
        Date dob = entity.getDob();
        if (dob == null) {
            values.putNull(COLUMN_DOB);
        } else {
            values.put(COLUMN_DOB
                    , Globals.getDTF().format(dob)
            );
        }
        return values;
    }

    @Override
    public T getByEmail(SQLiteDatabase database, String email) {
        Cursor cursor = database.query(
                getTableName()
                , getAllColumns(), COLUMN_EMAIL + " = '" + email + "' "
                , null, null, null, null
        );
        try {
            if (cursor.getCount() > 1) {
                Log.w(getClass().getName(), "More than one user for email: " + email + " found");
            }
            if (cursor.moveToFirst()) {
                return cursorToEntity(cursor, 0);
            }
            return null;
        } finally {
            cursor.close();
        }
    }
}
