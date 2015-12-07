package org.cmas.service.doc;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import com.google.myjson.Gson;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.BaseBeanContainer;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.dao.DataBaseHolder;
import org.cmas.dao.doc.DocFileDao;
import org.cmas.dao.doc.DocumentDao;
import org.cmas.entities.doc.DocFile;
import org.cmas.entities.doc.Document;
import org.cmas.json.EntityEditReply;
import org.cmas.json.doc.DocCreateReply;
import org.cmas.json.doc.DocumentDisplayModel;
import org.cmas.json.doc.DocumentSerializer;
import org.cmas.json.doc.DocumentViewerDisplayModel;
import org.cmas.remote.RemoteDocumentService;
import org.cmas.service.EntityDeleteService;
import org.cmas.service.LoginService;
import org.cmas.service.ReLoginAction;

import java.util.Date;
import java.util.List;

public class DocumentServiceImpl implements DocumentService, InitializingBean {

    private DocumentDao documentDao;
    private DocFileDao docFileDao;

    private RemoteDocumentService remoteDocumentService;
    private LoginService loginService;

    private EntityDeleteService entityDeleteService;

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        entityDeleteService = beanContainer.getEntityDeleteService();

        documentDao = beanContainer.getDocumentDao();
        docFileDao = beanContainer.getDocFileDao();
        remoteDocumentService = beanContainer.getRemoteDocumentService();
        loginService = beanContainer.getLoginService();
    }

    @Override
    public void loadUserDocs(
            final Context context
    ) {
        final long maxVersion;
        {
            DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
            SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
            try {
                maxVersion = documentDao.getMaxVersion(readableDatabase);
            } finally {
                readableDatabase.close();
            }
        }

        ReLoginAction<DocumentDisplayModel> reLoginAction = new ReLoginAction<DocumentDisplayModel>(loginService) {
            @Override
            protected Pair<DocumentDisplayModel, String> getRemoteResult() throws Exception {
                return remoteDocumentService.getUserDocs(context, maxVersion);
            }

            @Override
            protected Pair<DocumentDisplayModel, String> nullResultHandler(String errorMessage) {
                return new Pair<DocumentDisplayModel, String>(null, errorMessage);
            }

            @Override
            protected void okResultHandler(DocumentDisplayModel okResult) throws Exception {
                DataBaseHolder dbHolder = new DataBaseHolder(context);
                SQLiteDatabase writableDatabase = dbHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
                try {
                    writableDatabase.beginTransaction();
                    try {
                        for (DocumentViewerDisplayModel displayModel : okResult.getCreators()) {
//                            Profile profile = profileDao.getById(writableDatabase, displayModel.getId());
//                            if (profile == null) {
//                                Profile newProfile = new Profile();
//                                newProfile.setId(displayModel.getId());
//                                newProfile.setName(displayModel.getName());
//                                newProfile.setUserTypeId(displayModel.getTypeId());
//
//                                newProfile.setVersion(0L);
//
//                                profileDao.save(writableDatabase, newProfile);
//                            } else {
//                                profile.setName(displayModel.getName());
//                                profile.setUserTypeId(displayModel.getTypeId());
//                                profileDao.update(writableDatabase, profile);
//                            }
                        }

                        Gson gson = DocumentSerializer.createDocGSON();

                        for (Document document : okResult.getDocs()) {

                            docFileDao.deleteAllForDocument(context, document);
                            List<DocFile> docFiles = document.getFiles();
                            if (docFiles != null) {
                                for (DocFile docFile : docFiles) {
                                    docFileDao.save(context, document, docFile);
                                }
                            }

                            documentDao.saveOrUpdate(writableDatabase, document);
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
    public List<Document> getByProfileNoRemoteCall(Context context, long userId,String name) {
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
        try {
            return documentDao.getByUser(readableDatabase, userId, name);
        } finally {
            readableDatabase.close();
        }
    }

    @Override
    public Pair<DocCreateReply, String> addNewDoc(
            final Activity activity,
            final Document document) {

        ReLoginAction<DocCreateReply> reLoginAction = new ReLoginAction<DocCreateReply>(loginService) {
            @Override
            protected Pair<DocCreateReply, String> getRemoteResult() throws Exception {
                return remoteDocumentService.addNewDoc(
                        activity, document
                );
            }

            @Override
            protected Pair<DocCreateReply, String> nullResultHandler(String errorMessage) {
                return new Pair<DocCreateReply, String>(null, errorMessage);
            }

            @Override
            protected void okResultHandler(DocCreateReply okResult) throws Exception {
                DataBaseHolder dataBaseHolder = new DataBaseHolder(activity);
                SQLiteDatabase writableDatabase = dataBaseHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
                try {

                    document.setId(okResult.getId());
                    Date dateCreation = okResult.getDateCreation();
                    document.setDateCreation(dateCreation);
                    document.setVersion(okResult.getVersion());

                    documentDao.save(writableDatabase, document);

                    List<DocFile> docFiles = document.getFiles();
                    if (docFiles != null) {
                        for (DocFile docFile : docFiles) {
                            docFileDao.save(activity, document, docFile);
                        }
                    }
                } finally {
                    writableDatabase.close();
                }
            }
        };

        return reLoginAction.doAction(activity);
    }

    @Override
    public Pair<EntityEditReply, String> editDoc(
            final Context context,
            final Document document
    ) {

        ReLoginAction<EntityEditReply> reLoginAction = new ReLoginAction<EntityEditReply>(loginService) {
            @Override
            protected Pair<EntityEditReply, String> getRemoteResult() throws Exception {
                return remoteDocumentService.editDoc(
                        context, document
                );
            }

            @Override
            protected Pair<EntityEditReply, String> nullResultHandler(String errorMessage) {
                return new Pair<EntityEditReply, String>(null, errorMessage);
            }

            @Override
            protected void okResultHandler(EntityEditReply okResult) throws Exception {

                DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
                SQLiteDatabase writableDatabase = dataBaseHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
                try {
                    document.setVersion(okResult.getVersion());
                    documentDao.update(writableDatabase, document);
                } finally {
                    writableDatabase.close();
                }

                docFileDao.deleteAllForDocument(context, document);
                List<DocFile> docFiles = document.getFiles();
                if (docFiles != null) {
                    for (DocFile docFile : docFiles) {
                        docFileDao.save(context, document, docFile);
                    }
                }
            }
        };

        return reLoginAction.doAction(context);
    }

    @Override
    public Pair<EntityEditReply, String> deleteDoc(
            final Context context,
            final long documentId
    ) {
        ReLoginAction<EntityEditReply> reLoginAction = new ReLoginAction<EntityEditReply>(loginService) {
            @Override
            protected Pair<EntityEditReply, String> getRemoteResult() throws Exception {
                return remoteDocumentService.deleteDoc(
                        context, documentId
                );
            }

            @Override
            protected Pair<EntityEditReply, String> nullResultHandler(String errorMessage) {
                return new Pair<EntityEditReply, String>(null, errorMessage);
            }

            @Override
            protected void okResultHandler(EntityEditReply okResult) {
                removeDocument(context, documentId, okResult);
            }
        };

        return reLoginAction.doAction(context);
    }

    private void removeDocument(Context context,
                                long documentId,
                                EntityEditReply entityEditReply
    ) {
        DataBaseHolder dBHolder = new DataBaseHolder(context);
        SQLiteDatabase writableDatabase = dBHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
        try {
            writableDatabase.beginTransaction();
            try {
                Document document = documentDao.getById(writableDatabase, documentId);
                document.setVersion(entityEditReply.getVersion());
                entityDeleteService.deleteDocument(context, writableDatabase, document);
                writableDatabase.setTransactionSuccessful();
            } finally {
                writableDatabase.endTransaction();
            }
        } finally {
            writableDatabase.close();
        }
    }

}
