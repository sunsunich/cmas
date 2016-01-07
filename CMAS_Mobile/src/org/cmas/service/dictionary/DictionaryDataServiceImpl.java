package org.cmas.service.dictionary;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.BaseBeanContainer;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.R;
import org.cmas.dao.DataBaseHolder;
import org.cmas.dao.dictionary.DictionaryDataDao;
import org.cmas.dao.divespot.DiveSpotDao;
import org.cmas.entities.DictionaryEntity;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.remote.RemoteDictionaryService;
import org.cmas.util.ProgressTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DictionaryDataServiceImpl implements DictionaryDataService, InitializingBean {

    private RemoteDictionaryService remoteDictionaryService;

    private DiveSpotDao diveSpotDao;

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();

        remoteDictionaryService = beanContainer.getRemoteDictionaryService();
        diveSpotDao = beanContainer.getDiveSpotDao();
    }

    @Override
    public void loadDictionaryEntities(final Context context, ProgressTask.OnPublishProgressListener progressUpdateListener, int maxProgress) {
        String progressStatus = context.getString(R.string.loading_dictionaries);
        int tekProgress = 0;
        //если нужно каждый раз на разное значение увеличивать, то переменная не нужна
        int increasingProgress = (int) ((double) maxProgress * 0.2d);
        if (progressUpdateListener != null) {
            progressUpdateListener.onPublishProgress(progressStatus, String.valueOf(tekProgress));
        }

        //todo вынести в метод или вообще вынести в listener?
        if (progressUpdateListener != null) {
            tekProgress += increasingProgress;
            progressUpdateListener.onPublishProgress(progressStatus, String.valueOf(increasingProgress));
        }

        if (progressUpdateListener != null) {
            tekProgress += increasingProgress;
            progressUpdateListener.onPublishProgress(progressStatus, String.valueOf(increasingProgress));
        }

        loadEntity(
                context,
                diveSpotDao,
                new RemoteListEntityGetter<DiveSpot>() {
                    @Override
                    public Pair<List<DiveSpot>, String> getEntitiesList(long maxVersion)
                            throws Exception {
                        return remoteDictionaryService.getDiveSpots(context, maxVersion);
                    }
                }
        );


        if (progressUpdateListener != null) {
            tekProgress += increasingProgress;
            progressUpdateListener.onPublishProgress(progressStatus, String.valueOf(increasingProgress));
        }

        //в конце добавляем ровно столько, сколько не хватает до максимального прогресса.
        //пока не смотрим, чтобы устанавливаемый прогресс превышал максимальный. разраб пусть сам следит.
        if (progressUpdateListener != null) {
            //tekProgress+=0.2d*maxProgress;
            progressUpdateListener.onPublishProgress(context.getString(R.string.loading_dictionaries_complete), String.valueOf(maxProgress - tekProgress));
        }
    }

    private <T extends DictionaryEntity> void loadEntity(
            Context context,
            DictionaryDataDao<T> dao,
            final RemoteListEntityGetter<T> entityGetter
    ) {
        loadEntities(
                context,
                Arrays.asList(dao),
                new RemoteListListEntityGetter() {
                    @Override
                    public Pair<List<List<? extends DictionaryEntity>>, String> getEntitiesList(List<Long> maxVersions)
                            throws Exception {
                        Pair<? extends List<? extends DictionaryEntity>, String> result = entityGetter.getEntitiesList(maxVersions.get(0));
                        List<List<? extends DictionaryEntity>> lists = new ArrayList<List<? extends
                            DictionaryEntity>>();
                        lists.add(result.first);
                       // (List<List<? extends DictionaryEntity>>)
                       // Arrays.asList(result.first);
                        return new Pair<List<List<? extends DictionaryEntity>>, String>(
                                lists, result.second
                        );
                    }
                }
        );
    }

    private void loadEntities(
            Context context,
            List<? extends DictionaryDataDao<? extends DictionaryEntity>> daos,
            RemoteListListEntityGetter entityGetter
    ) {
        try {
            List<Long> maxVersions = new ArrayList<Long>(daos.size());
            DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
            SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
            try {
                for (DictionaryDataDao<? extends DictionaryEntity> dao : daos) {
                    maxVersions.add(dao.getMaxVersion(readableDatabase));
                }
            } finally {
                readableDatabase.close();
            }

            Pair<List<List<? extends DictionaryEntity>>, String> result = entityGetter.getEntitiesList(
                    maxVersions
            );

            List<List<? extends DictionaryEntity>> entitiesLists = result.first;
            if (entitiesLists == null) {
                String message = "Failed to load Entities, cause: " + result.second;
                Log.e(DictionaryDataServiceImpl.class.getName(), message);
            } else {
                for (int i = 0, daosSize = daos.size(); i < daosSize; i++) {
                    DictionaryDataDao dao = daos.get(i);
                    List<? extends DictionaryEntity> entities = entitiesLists.get(i);
                    persistEntities(context, entities, dao);
                }
            }


        } catch (Exception e) {
            String message = "Failed to load Entities, cause: " + e.getMessage();
            Log.e(DictionaryDataServiceImpl.class.getName(), message, e);
        }
    }

    private <T extends DictionaryEntity> void persistEntities(
            Context context, List<T> entities, DictionaryDataDao<T> dao) {
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase writableDatabase = dataBaseHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
        try {
            writableDatabase.beginTransaction();
            try {
                execPersistQueries(writableDatabase, dao, entities);
                writableDatabase.setTransactionSuccessful();
            } finally {
                writableDatabase.endTransaction();
            }
        } finally {
            writableDatabase.close();
        }
    }

    public static <T extends DictionaryEntity> void execPersistQueries(
            SQLiteDatabase writableDatabase, DictionaryDataDao<T> dao, Collection<T> entities) {
        if (entities != null) {
            for (T entity : entities) {
                if (entity.isDeleted()) {
                    dao.delete(writableDatabase, entity);
                } else {
                    dao.saveOrUpdate(writableDatabase, entity);
                }
            }
        }
    }
}
