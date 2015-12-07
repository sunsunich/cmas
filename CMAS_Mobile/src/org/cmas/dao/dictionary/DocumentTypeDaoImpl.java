package org.cmas.dao.dictionary;

import android.content.ContentValues;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.entities.doc.DocumentType;
import org.cmas.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class DocumentTypeDaoImpl extends DictionaryDataDaoImpl<DocumentType> implements DocumentTypeDao {

    public static final String DOCUMENT_TYPE_TABLE = "document_types";

    public static final String COLUMN_LOCAL_NAME = "local_name";

    // Database creation SQL statement
    private static final String CREATE_TABLE_ENDING_QUERY =
            ", " + COLUMN_LOCAL_NAME + " text not null"
                    + ");";

    private static final String[] ADDITIONAL_COLUMNS = {
            COLUMN_LOCAL_NAME
    };

    @Override
    protected String getTableCreateQueryEnding() {
        return CREATE_TABLE_ENDING_QUERY;
    }

    @Override
    public String getTableAlias() {
        return "dt";
    }

    @Override
    protected DocumentType constructEntity() {
        return new DocumentType();
    }

    @Override
    public String getTableName() {
        return DOCUMENT_TYPE_TABLE;
    }

    @Override
    public String[] getAllColumns() {
        return StringUtil.concatArrays(super.getAllColumns(), ADDITIONAL_COLUMNS);
    }

    @Override
    public DocumentType cursorToEntity(Cursor cursor, int index) {
        DocumentType entity = super.cursorToEntity(cursor, index);
        int i = index + super.getAllColumns().length;
        entity.setLocalName(cursor.getString(i));
        return entity;
    }

    @Override
    protected ContentValues entityToContentValues(DocumentType entity) {
        ContentValues values = super.entityToContentValues(entity);
        String localName = entity.getLocalName();
        values.put(COLUMN_LOCAL_NAME, localName == null ? "" : localName);
        return values;
    }

    @Override
    public List<DocumentType> getAllNoDeleted(SQLiteDatabase database) {
        Cursor cursor = database.query(
                DOCUMENT_TYPE_TABLE
                , getAllColumns(), COLUMN_DELETED + " = 0"
                , null, null, null, null
        );
        try {
            List<DocumentType> result = new ArrayList<DocumentType>(cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    DocumentType document = cursorToEntity(cursor, 0);
                    result.add(document);
                } while (cursor.moveToNext());
            }

            return result;
        } finally {
            cursor.close();
        }
    }
}
