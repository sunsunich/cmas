package org.cmas.dao.dictionary;

import android.content.ContentValues;
import android.database.Cursor;
import org.cmas.entities.Country;
import org.cmas.util.StringUtil;

/**
 * Created on Jan 06, 2016
 *
 * @author Alexander Petukhov
 */
public class CountryDaoImpl extends DictionaryDataDaoImpl<Country> implements CountryDao {

    public static final String COUNTRIES_TABLE = "countries";

    private static final String CODE = "code";

    private static final String CREATE_TABLE_ENDING_QUERY =
            ", " + CODE + " text not null"
             + ");";

    private static final String[] ADDITIONAL_COLUMNS = {
            CODE
    };

    @Override
    protected String getTableCreateQueryEnding() {
        return CREATE_TABLE_ENDING_QUERY;
    }

    @Override
    public String[] getAllColumns() {
        return StringUtil.concatArrays(super.getAllColumns(), ADDITIONAL_COLUMNS);
    }

    @Override
    public Country cursorToEntity(Cursor cursor, int index) {
        Country entity = super.cursorToEntity(cursor, index);
        int i = index + super.getAllColumns().length;
        entity.setCode(cursor.getString(i));
        return entity;

    }

    @Override
    protected ContentValues entityToContentValues(Country entity) {
        ContentValues values = super.entityToContentValues(entity);
        values.put(CODE, entity.getCode());
        return values;
    }

    @Override
    protected Country constructEntity() {
        return new Country();
    }

    @Override
    public String getTableAlias() {
        return "ds";
    }

    @Override
    public String getTableName() {
        return COUNTRIES_TABLE;
    }
}
