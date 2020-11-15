package org.cmas.service.dictionary;

import org.cmas.util.ProgressListener;

public interface DictionaryDataService {

    void loadDictionaryEntities(ProgressListener progressUpdateListener,
                                int startProgress,
                                int endProgress
    ) throws Exception;

//    <T extends DictionaryEntity> List<T> getAllDictionaryEntities(DictionaryDataDao<T> dao);
//
//    <T extends DictionaryEntity> T getByName(DictionaryDataDao<T> dao, String name);
}
