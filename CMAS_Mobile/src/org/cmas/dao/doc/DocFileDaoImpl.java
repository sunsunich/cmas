package org.cmas.dao.doc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import org.cmas.dao.GeneralDaoImpl;
import org.cmas.entities.doc.DocFile;
import org.cmas.entities.doc.Document;

import java.io.File;
import java.util.List;

public class DocFileDaoImpl extends GeneralDaoImpl<DocFile> implements DocFileDao {

    static final String DOC_FILE_TABLE = "user";

    static final String COLUMN_NAME = "name";
    private static final String COLUMN_EXT = "ext";
    private static final String COLUMN_DOCUMENT_ID = "type_id";

    // Database creation SQL statement
    private static final String CREATE_TABLE_QUERY = "create table "
            + DOC_FILE_TABLE
            + "( "
            + COLUMN_ID + " integer primary key, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_EXT + " text not null, "
            + COLUMN_DOCUMENT_ID + " integer not null, "
            + "FOREIGN KEY (" + COLUMN_DOCUMENT_ID + ") REFERENCES "
            + DocumentDaoImpl.DOCUMENT_TABLE + " (" + COLUMN_ID + ')'
            + ");";

    private static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_EXT,
            COLUMN_DOCUMENT_ID
    };

    @Override
    public String save(Context context, Document document, DocFile docFile) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Context context, String fileName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteAllForDocument(Context context, Document document) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<File> getFiles(Context context, Document document) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getDataBaseCreateQuery() {
        return CREATE_TABLE_QUERY;
    }

    @Override
    public String getTableName() {
        return DOC_FILE_TABLE;
    }

    @Override
    public String[] getAllColumns() {
        return ALL_COLUMNS;
    }

    @Override
    public DocFile cursorToEntity(Cursor cursor, int index) {
        DocFile entity = new DocFile();
        int i = index;
        entity.setId(cursor.getLong(i));
        i++;
        entity.setName(cursor.getString(i));
        i++;
        entity.setExt(cursor.getString(i));
        i++;
        entity.setDocumentId(cursor.getLong(i));
        return entity;
    }

    @Override
    protected ContentValues entityToContentValues(DocFile entity) {
        ContentValues values = super.entityToContentValues(entity);
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_EXT, entity.getExt());
        values.put(COLUMN_DOCUMENT_ID, entity.getDocumentId());
        return values;
    }
}
