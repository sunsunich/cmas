package org.cmas.dao.logbook;

import android.content.ContentValues;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.dao.DiverDaoImpl;
import org.cmas.dao.dictionary.DictionaryDataDaoImpl;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"MagicCharacter", "StringConcatenation"})
public class LogbookEntryDaoImpl extends DictionaryDataDaoImpl<LogbookEntry> implements LogbookEntryDao, InitializingBean {

    public static final String LOGBOOK_ENTRY_TABLE = "logbook_entries";

    private static final String COLUMN_DATE_CREATION = "date_creation";
    private static final String COLUMN_SEARCH_NAME = "search_name";
    private static final String COLUMN_NOTE = "note";

    public static final String COLUMN_DIVER_ID = "diver_id";


    private static final String CREATE_TABLE_ENDING_QUERY =
            ", " + COLUMN_DATE_CREATION + " text, "
                    + COLUMN_SEARCH_NAME + " text not null collate nocase, "
                    + COLUMN_NOTE + " text, "

                    + COLUMN_DIVER_ID + " integer, "
                    + "FOREIGN KEY (" + COLUMN_DIVER_ID + ") REFERENCES "
                    + DiverDaoImpl.DIVER_TABLE + " (" + COLUMN_ID + ") "
                    + ");";

    private static final String[] ADDITIONAL_COLUMNS = {
            COLUMN_DATE_CREATION,
            COLUMN_NOTE,
            COLUMN_SEARCH_NAME,
            COLUMN_DIVER_ID
    };


    @Override
    protected String getTableCreateQueryEnding() {
        return CREATE_TABLE_ENDING_QUERY;
    }

    @Override
    public String getTableAlias() {
        return "lb";
    }

    @Override
    protected LogbookEntry constructEntity() {
        return new LogbookEntry();
    }

    @Override
    public String getTableName() {
        return LOGBOOK_ENTRY_TABLE;
    }

    @Override
    public String[] getAllColumns() {
        return StringUtil.concatArrays(super.getAllColumns(), ADDITIONAL_COLUMNS);
    }

    private String getDocumentsByDiverSql;
    private String getDocumentsByDiverAndNameSql;

    @Override
    public void initialize() {
        String dAlias = getTableAlias();
        getDocumentsByDiverSql = "select distinct " + getAllColumnsStr()
                + " from " + LOGBOOK_ENTRY_TABLE + " as " + dAlias
                + " where " + dAlias + '.' + COLUMN_DIVER_ID + " = ?"
                + " and " + dAlias + '.' + COLUMN_DELETED + " = 0"
                + " order by " + COLUMN_DATE_CREATION + " desc "
        ;
        getDocumentsByDiverAndNameSql = "select distinct " + getAllColumnsStr()
                + " from " + LOGBOOK_ENTRY_TABLE + " as " + dAlias
                + " where " + dAlias + '.' + COLUMN_DIVER_ID + " = ?"
                + " and " + dAlias + '.' + COLUMN_DELETED + " = 0"
                + " and " + dAlias + '.' + COLUMN_SEARCH_NAME + " like ?"
                + " order by " + COLUMN_DATE_CREATION + " desc "
        ;
    }

    @Override
    public LogbookEntry cursorToEntity(Cursor cursor, int index) {
        try {
            LogbookEntry entity = super.cursorToEntity(cursor, index);
            int i = index + super.getAllColumns().length;
            SimpleDateFormat documentDateFormat = Globals.getDocumentDateFormat();
            String dateCreation = cursor.getString(i);
            if (dateCreation != null) {
                entity.setDateCreation(documentDateFormat.parse(dateCreation));
            }
            i++;
            entity.setNote(cursor.getString(i));
            return entity;
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected ContentValues entityToContentValues(LogbookEntry entity) {
        ContentValues values = super.entityToContentValues(entity);
        SimpleDateFormat dateFormat = Globals.getDocumentDateFormat();
        Date dateCreation = entity.getDateCreation();
        if (dateCreation == null) {
            values.putNull(COLUMN_DATE_CREATION);
        } else {
            values.put(COLUMN_DATE_CREATION
                    , dateFormat.format(dateCreation)
            );
        }
        values.put(COLUMN_NOTE, entity.getNote());
        String name = entity.getName();
        //noinspection StringToUpperCaseOrToLowerCaseWithoutLocale
        values.put(COLUMN_SEARCH_NAME, name == null ? "" : name.toUpperCase());
        return values;
    }

    @SuppressWarnings("resource")
    @Override
    public List<LogbookEntry> getByUser(SQLiteDatabase database, long diverId, String name) {
        Cursor cursor;
        if (name == null) {
            cursor = database.rawQuery(
                    getDocumentsByDiverSql, new String[]{String.valueOf(diverId)}
            );
        } else {
            //noinspection StringToUpperCaseOrToLowerCaseWithoutLocale
            cursor = database.rawQuery(
                    getDocumentsByDiverAndNameSql, new String[]{
                            String.valueOf(diverId), '%' + name.toUpperCase() + '%'
                    }
            );
        }
        try {
            List<LogbookEntry> result = new ArrayList<>(cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    LogbookEntry logbookEntry = cursorToEntity(cursor, 0);
                    result.add(logbookEntry);
                } while (cursor.moveToNext());
            }

            return result;
        } finally {
            cursor.close();
        }
    }
}
