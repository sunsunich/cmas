package org.cmas.service.dictionary;

import android.content.Context;
import org.cmas.dao.dictionary.DictionaryDataDao;
import org.cmas.entities.DictionaryEntity;
import org.cmas.util.ProgressTask;

import java.util.List;

public interface DictionaryDataService {

    void loadDictionaryEntities(Context context, ProgressTask.OnPublishProgressListener progressUpdateListener, int maxProgress);

    <T extends DictionaryEntity> List<T> getAllDictionaryEntities(
            Context context, DictionaryDataDao<T> dao);

    <T extends DictionaryEntity> T getByName(
            Context context, DictionaryDataDao<T> dao, String name);
}
