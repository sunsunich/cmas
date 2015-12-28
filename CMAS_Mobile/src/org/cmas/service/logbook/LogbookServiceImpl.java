package org.cmas.service.logbook;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.BaseBeanContainer;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.dao.DataBaseHolder;
import org.cmas.dao.logbook.LogbookEntryDao;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.json.EntityEditReply;
import org.cmas.json.logbook.LogbookEntryCreateReply;
import org.cmas.remote.RemoteLogbookService;
import org.cmas.service.EntityDeleteService;
import org.cmas.service.LoginService;
import org.cmas.service.ReLoginAction;

import java.util.Date;
import java.util.List;

public class LogbookServiceImpl implements LogbookService, InitializingBean {

    private LogbookEntryDao logbookEntryDao;
//    private DocFileDao docFileDao;

    private RemoteLogbookService remoteLogbookService;
    private LoginService loginService;

    private EntityDeleteService entityDeleteService;

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        entityDeleteService = beanContainer.getEntityDeleteService();

        logbookEntryDao = beanContainer.getLogbookEntryDao();
//        docFileDao = beanContainer.getDocFileDao();
        remoteLogbookService = beanContainer.getRemoteDocumentService();
        loginService = beanContainer.getLoginService();
    }

    @Override
    public void loadLogbook(
            final Context context,
            final long diverId
    ) {
        final long maxVersion;
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
        try {
            maxVersion = logbookEntryDao.getMaxVersion(readableDatabase);
        } finally {
            readableDatabase.close();
        }

        ReLoginAction<List<LogbookEntry>> reLoginAction = new ReLoginAction<List<LogbookEntry>>(loginService) {
            @Override
            protected Pair<List<LogbookEntry>, String> getRemoteResult() throws Exception {
                return remoteLogbookService.getDiverLogbookEntries(context, maxVersion, diverId);
            }

            @Override
            protected Pair<List<LogbookEntry>, String> nullResultHandler(String errorMessage) {
                return new Pair<>(null, errorMessage);
            }

            @Override
            protected void okResultHandler(List<LogbookEntry> okResult) throws Exception {
                DataBaseHolder dbHolder = new DataBaseHolder(context);
                SQLiteDatabase writableDatabase = dbHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
                try {
                    writableDatabase.beginTransaction();
                    try {
                        for (LogbookEntry logbookEntry : okResult) {

//                            docFileDao.deleteAllForDocument(context, document);
//                            List<DocFile> docFiles = document.getFiles();
//                            if (docFiles != null) {
//                                for (DocFile docFile : docFiles) {
//                                    docFileDao.save(context, document, docFile);
//                                }
//                            }

                            logbookEntryDao.saveOrUpdate(writableDatabase, logbookEntry);
                        }
                        writableDatabase.setTransactionSuccessful();
                    } finally {
                        writableDatabase.endTransaction();
                    }
                } finally {
                    writableDatabase.close();
                }
            }
        };
        reLoginAction.doAction(context);
    }

    @Override
    public List<LogbookEntry> getByDiverNoRemoteCall(Context context, long diverId, String name) {
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
        try {
            return logbookEntryDao.getByUser(readableDatabase, diverId, name);
        } finally {
            readableDatabase.close();
        }
    }

    @Override
    public Pair<LogbookEntryCreateReply, String> addNewEntry(
            final Activity activity,
            final LogbookEntry logbookEntry) {

        ReLoginAction<LogbookEntryCreateReply> reLoginAction = new ReLoginAction<LogbookEntryCreateReply>(loginService) {
            @Override
            protected Pair<LogbookEntryCreateReply, String> getRemoteResult() throws Exception {
                return remoteLogbookService.addNewEntry(
                        activity, logbookEntry
                );
            }

            @Override
            protected Pair<LogbookEntryCreateReply, String> nullResultHandler(String errorMessage) {
                return new Pair<>(null, errorMessage);
            }

            @Override
            protected void okResultHandler(LogbookEntryCreateReply okResult) throws Exception {
                DataBaseHolder dataBaseHolder = new DataBaseHolder(activity);
                SQLiteDatabase writableDatabase = dataBaseHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
                try {

                    logbookEntry.setId(okResult.getId());
                    Date dateCreation = okResult.getDateCreation();
                    logbookEntry.setDateCreation(dateCreation);
                    logbookEntry.setVersion(okResult.getVersion());

                    logbookEntryDao.save(writableDatabase, logbookEntry);

//                    List<DocFile> docFiles = logbookEntry.getFiles();
//                    if (docFiles != null) {
//                        for (DocFile docFile : docFiles) {
//                            docFileDao.save(activity, logbookEntry, docFile);
//                        }
//                    }
                } finally {
                    writableDatabase.close();
                }
            }
        };

        return reLoginAction.doAction(activity);
    }

    @Override
    public Pair<EntityEditReply, String> editEntry(
            final Context context,
            final LogbookEntry logbookEntry
    ) {

        ReLoginAction<EntityEditReply> reLoginAction = new ReLoginAction<EntityEditReply>(loginService) {
            @Override
            protected Pair<EntityEditReply, String> getRemoteResult() throws Exception {
                return remoteLogbookService.editEntry(
                        context, logbookEntry
                );
            }

            @Override
            protected Pair<EntityEditReply, String> nullResultHandler(String errorMessage) {
                return new Pair<>(null, errorMessage);
            }

            @Override
            protected void okResultHandler(EntityEditReply okResult) throws Exception {

                DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
                SQLiteDatabase writableDatabase = dataBaseHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
                try {
                    logbookEntry.setVersion(okResult.getVersion());
                    logbookEntryDao.update(writableDatabase, logbookEntry);
                } finally {
                    writableDatabase.close();
                }

//                docFileDao.deleteAllForDocument(context, logbookEntry);
//                List<DocFile> docFiles = logbookEntry.getFiles();
//                if (docFiles != null) {
//                    for (DocFile docFile : docFiles) {
//                        docFileDao.save(context, logbookEntry, docFile);
//                    }
//                }
            }
        };

        return reLoginAction.doAction(context);
    }

    @Override
    public Pair<EntityEditReply, String> deleteEntry(
            final Context context,
            final long entryId
    ) {
        ReLoginAction<EntityEditReply> reLoginAction = new ReLoginAction<EntityEditReply>(loginService) {
            @Override
            protected Pair<EntityEditReply, String> getRemoteResult() throws Exception {
                return remoteLogbookService.deleteEntry(
                        context, entryId
                );
            }

            @Override
            protected Pair<EntityEditReply, String> nullResultHandler(String errorMessage) {
                return new Pair<>(null, errorMessage);
            }

            @Override
            protected void okResultHandler(EntityEditReply okResult) {
                removeDocument(context, entryId, okResult);
            }
        };

        return reLoginAction.doAction(context);
    }

    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private void removeDocument(Context context,
                                long documentId,
                                EntityEditReply entityEditReply
    ) {
        DataBaseHolder dBHolder = new DataBaseHolder(context);
        SQLiteDatabase writableDatabase = dBHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
        try {
            writableDatabase.beginTransaction();
            try {
                LogbookEntry logbookEntry = logbookEntryDao.getById(writableDatabase, documentId);
                logbookEntry.setVersion(entityEditReply.getVersion());
                entityDeleteService.deleteLogbookEntry(context, writableDatabase, logbookEntry);
                writableDatabase.setTransactionSuccessful();
            } finally {
                writableDatabase.endTransaction();
            }
        } finally {
            writableDatabase.close();
        }
    }

}
