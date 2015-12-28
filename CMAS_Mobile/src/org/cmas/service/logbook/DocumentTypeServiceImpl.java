package org.cmas.service.logbook;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.BaseBeanContainer;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.dao.DataBaseHolder;
import org.cmas.dao.dictionary.DocumentTypeDao;
import org.cmas.entities.doc.DocumentType;

import java.util.List;

public class DocumentTypeServiceImpl implements DocumentTypeService, InitializingBean {

    private DocumentTypeDao documentTypeDao;

    @Override
    public void initialize() {
        documentTypeDao = BaseBeanContainer.getInstance().getDocumentTypeDao();
    }

    @Override
    public List<DocumentType> getAllNoDeleted(Context context) {
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
        try {
            return documentTypeDao.getAllNoDeleted(readableDatabase);
        } finally {
            readableDatabase.close();
        }
    }

    @Override
    public DocumentType getById(Context context, long id) {
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
        try {
            return documentTypeDao.getById(readableDatabase, id);
        } finally {
            readableDatabase.close();
        }
    }
}
