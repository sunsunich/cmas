package org.cmas.service.dictionary;

import org.apache.commons.lang3.tuple.Pair;
import org.cmas.BaseBeanContainer;
import org.cmas.InitializingBean;
import org.cmas.android.i18n.ErrorCodesManager;
import org.cmas.entities.Country;
import org.cmas.entities.DictionaryEntity;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.remote.NetworkUnavailableException;
import org.cmas.remote.RemoteDictionaryService;
import org.cmas.service.EntityPersister;
import org.cmas.service.RemoteListEntityGetter;
import org.cmas.service.VersionableEntityPersister;
import org.cmas.util.ProgressListener;
import org.cmas.util.TaskProgressUpdate;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DictionaryDataServiceImpl implements DictionaryDataService, InitializingBean {

    private RemoteDictionaryService remoteDictionaryService;
    private ErrorCodesManager errorCodesManager;
    private VersionableEntityPersister<Country> countryPersister;
    private VersionableEntityPersister<NationalFederation> federationPersister;
    private final List<VersionableEntityPersister<?>> allPersisters = new ArrayList<>();

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();

        remoteDictionaryService = beanContainer.getRemoteDictionaryService();
        errorCodesManager = beanContainer.getErrorCodesManager();
        countryPersister = beanContainer.getCountryPersister();
        federationPersister = beanContainer.getFederationPersister();
        allPersisters.add(countryPersister);
        allPersisters.add(federationPersister);
    }

    @Override
    public void loadDictionaryEntities(@Nullable ProgressListener progressListener,
                                       int startProgress,
                                       int endProgress

    ) throws Exception {
        String progressStatus = "loading_dictionaries";
        int currentProgress = startProgress;
        publishProgress(progressListener, progressStatus, currentProgress);
        int entityCnt = allPersisters.size() + 1; /* errorCodesManager */
        int increasingProgress = (int) ((double) (endProgress - startProgress) / (double) entityCnt);
        try {
            persistEntities(
                    errorCodesManager, remoteDictionaryService.getErrorCodes()
            );
            errorCodesManager.loadErrorCodesToMemory();

            loadEntity(
                    maxVersion -> remoteDictionaryService.getCountries(maxVersion),
                    countryPersister
            );
            currentProgress += increasingProgress;
            publishProgress(progressListener, progressStatus, currentProgress);
            loadEntity(
                    maxVersion -> remoteDictionaryService.getNationalFederations(maxVersion),
                    federationPersister
            );
            currentProgress += increasingProgress;
            publishProgress(progressListener, progressStatus, currentProgress);

//        loadEntity(
//                new RemoteListEntityGetter<DiveSpot>() {
//                    @Override
//                    public Pair<List<DiveSpot>, String> getEntitiesList(long maxVersion)
//                            throws Exception {
//                        return remoteDictionaryService.getDiveSpots(context, maxVersion);
//                    }
//                }
//        );
//
//        publishProgress(progressListener, progressStatus, currentProgress);
        } catch (NetworkUnavailableException e) {
            if (!errorCodesManager.hasData()) {
                errorCodesManager.loadErrorCodesToMemory();
            }
            if (hasLocalDictionaryData()) {
                publishProgress(progressListener, "offline_mode", endProgress);
            } else {
                throw e;
            }
        }
        publishProgress(progressListener, "loading_dictionaries_complete", endProgress);
    }

    private boolean hasLocalDictionaryData() {
        boolean hasLocalData = !allPersisters.isEmpty();
        for (VersionableEntityPersister<?> persister : allPersisters) {
            boolean hasDataForPersister = persister.getMaxVersion() > 0L;
            hasLocalData = hasLocalData && hasDataForPersister;
        }
        return hasLocalData && errorCodesManager.hasData();
    }

    private static <T extends DictionaryEntity> void loadEntity(
            RemoteListEntityGetter<T> entityGetter,
            VersionableEntityPersister<T> persister
    ) throws Exception {
        Pair<List<T>, String> result;
        try {
            long maxVersion = persister.getMaxVersion();
            result = entityGetter.getEntitiesList(maxVersion);
        } catch (Exception e) {
            String message = "Failed to load Entities, cause: " + e.getMessage();
            throw new Exception(message, e);
        }
        persistEntities(persister, result);
    }

    private static <T> void persistEntities(
            EntityPersister<T> persister, Pair<List<T>, String> result
    ) throws Exception {
        List<T> entities = result.getLeft();
        if (entities == null) {
            String message = "Failed to load Entities, cause: " + result.getRight();
            throw new Exception(message);
        } else {
            persister.persist(entities);
        }
    }

    private static void publishProgress(@Nullable ProgressListener progressListener,
                                        String progressStatus,
                                        int currentProgress) {
        if (progressListener != null) {
            progressListener.publishProgress(new TaskProgressUpdate(currentProgress, progressStatus));
        }
    }
}
