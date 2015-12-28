package org.cmas.service;


import android.content.Context;
import android.content.SharedPreferences;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.BaseBeanContainer;
import org.cmas.InitializingBean;
import org.cmas.Settings;
import org.cmas.SettingsService;
import org.cmas.dao.DataBaseHolder;
import org.cmas.dao.doc.DocFileDao;
import org.cmas.dao.doc.DocumentDao;
import org.cmas.dao.logbook.LogbookEntryDao;
import org.cmas.entities.doc.Document;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.util.android.SecurePreferences;

public class EntityDeleteServiceImpl implements EntityDeleteService, InitializingBean {


    private LogbookEntryDao logbookEntryDao;

    private DocumentDao documentDao;
    private DocFileDao docFileDao;

    @Override
    public void initialize() {

        BaseBeanContainer baseBeanContainer = BaseBeanContainer.getInstance();
        logbookEntryDao = baseBeanContainer.getLogbookEntryDao();

        documentDao = baseBeanContainer.getDocumentDao();
        docFileDao = baseBeanContainer.getDocFileDao();
    }

//    @Override
//    public void deleteProfile(Context context, SQLiteDatabase database, User user) {
//        long userId = user.getId();
//
//        List<Document> documents = documentDao.getByUser(database, userId, null);
//        for (Document logbookEntry : documents) {
//
//                deleteDocument(context, database, logbookEntry);
//
//        }
//
//        u.delete(database, user);
//    }

    @Override
    public void deleteDocument(Context context, SQLiteDatabase database, Document document) {
        docFileDao.deleteAllForDocument(context, document);

        documentDao.delete(database, document);
    }

    @Override
    public void deleteLogbookEntry(Context context, SQLiteDatabase database, LogbookEntry logbookEntry) {
        logbookEntryDao.delete(database, logbookEntry);
    }

    @Override
    public void deleteAllData(Context context) {
        context.deleteDatabase(DataBaseHolder.getDBFullPath(context));

        SharedPreferences sharedPreferences = new SecurePreferences(context);
        SettingsService settingsService= BaseBeanContainer.getInstance().getSettingsService();
        Settings settings=settingsService.getSettings(sharedPreferences);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
        settings.clear();
        settingsService.setSettings(sharedPreferences,settings);
    }
}
