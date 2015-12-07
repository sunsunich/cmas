package org.cmas.dao.doc;

import android.content.ContentValues;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.dao.dictionary.DictionaryDataDaoImpl;
import org.cmas.dao.dictionary.DocumentTypeDaoImpl;
import org.cmas.entities.doc.Document;
import org.cmas.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DocumentDaoImpl extends DictionaryDataDaoImpl<Document> implements DocumentDao, InitializingBean {

    public static final String DOCUMENT_TABLE = "document";

    private static final String COLUMN_DATE_CREATION = "date_creation";
    private static final String COLUMN_SEARCH_NAME = "search_name";
    public static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DOCUMENT_TYPE_ID = "document_type_id";


    private static final String CREATE_TABLE_ENDING_QUERY =
            ", " + COLUMN_DATE_CREATION + " text, "
                    + COLUMN_SEARCH_NAME + " text not null collate nocase, "
                    + COLUMN_DESCRIPTION + " text, "
                    + COLUMN_DOCUMENT_TYPE_ID + " integer, "

                    + "FOREIGN KEY (" + COLUMN_DOCUMENT_TYPE_ID + ") REFERENCES "
                    + DocumentTypeDaoImpl.DOCUMENT_TYPE_TABLE + " (" + COLUMN_ID + ") "
                    + ");";

    private static final String[] ADDITIONAL_COLUMNS = {
            COLUMN_DATE_CREATION,
            COLUMN_DESCRIPTION,
            COLUMN_DOCUMENT_TYPE_ID,
            COLUMN_SEARCH_NAME
    };


    @Override
    protected String getTableCreateQueryEnding() {
        return CREATE_TABLE_ENDING_QUERY;
    }

    @Override
    public String getTableAlias() {
        return "d";
    }

    @Override
    protected Document constructEntity() {
        return new Document();
    }

    @Override
    public String getTableName() {
        return DOCUMENT_TABLE;
    }

    @Override
    public String[] getAllColumns() {
        return StringUtil.concatArrays(super.getAllColumns(), ADDITIONAL_COLUMNS);
    }

//    private String getDocumentsByProfileSql;
//    private String getDocumentsByProfileAndNameSql;

    @Override
    public void initialize() {
        String dAlias = getTableAlias();
//        getDocumentsByProfileSql = "select distinct " + getAllColumnsStr()
//                + " from " + DOCUMENT_TABLE + " as " + dAlias
//                + " where (" + dAlias + '.' + COLUMN_AUTHOR_ID + " = ?"
//                + " or " + dAlias + '.' + COLUMN_CREATOR_ID + " = ?)"
//                + " and " + dAlias + '.' + COLUMN_DELETED + " = 0"
//                + " order by " + COLUMN_DATE_CREATION + " desc "
//        ;
//        getDocumentsByProfileAndNameSql = "select distinct " + getAllColumnsStr()
//                + " from " + DOCUMENT_TABLE + " as " + dAlias
//                + " where (" + dAlias + '.' + COLUMN_AUTHOR_ID + " = ?"
//                + " or " + dAlias + '.' + COLUMN_CREATOR_ID + " = ?)"
//                + " and " + dAlias + '.' + COLUMN_DELETED + " = 0"
//                + " and " + dAlias + '.' + COLUMN_SEARCH_NAME + " like ?"
//                + " order by " + COLUMN_DATE_CREATION + " desc "
        ;
    }

    @Override
    public Document cursorToEntity(Cursor cursor, int index) {
        try {
            Document entity = super.cursorToEntity(cursor, index);
            int i = index + super.getAllColumns().length;
            SimpleDateFormat documentDateFormat = Globals.getDocumentDateFormat();
            String dateCreation = cursor.getString(i);
            if (dateCreation != null) {
                entity.setDateCreation(documentDateFormat.parse(dateCreation));
            }
            i++;
            entity.setDescription(cursor.getString(i));
            i++;
            entity.setTypeId(cursor.getLong(i));
            return entity;
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected ContentValues entityToContentValues(Document entity) {
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
        values.put(COLUMN_DESCRIPTION, entity.getDescription());
        values.put(COLUMN_DOCUMENT_TYPE_ID, entity.getTypeId());
        String name = entity.getName();
        values.put(COLUMN_SEARCH_NAME, name == null ? "" : name.toUpperCase());
        return values;
    }

    @Override
    public List<Document> getByUser(SQLiteDatabase database, long userId, String name) {
//        Cursor cursor;
//        if (name == null) {
//            cursor = database.rawQuery(
//                    getDocumentsByProfileSql, new String[]{String.valueOf(userId), String.valueOf(userId)}
//            );
//        } else {
//            cursor = database.rawQuery(
//                    getDocumentsByProfileAndNameSql, new String[]{String.valueOf(userId), String.valueOf(userId), '%' + name.toUpperCase() + '%'}
//            );
//        }
//        try {
//            List<Document> result = new ArrayList<Document>(cursor.getCount());
//            if (cursor.moveToFirst()) {
//                do {
//                    Document document = cursorToEntity(cursor, 0);
//                    result.add(document);
//                } while (cursor.moveToNext());
//            }
//
//            return result;
//        } finally {
//            cursor.close();
//        }
        return null;
    }
}
