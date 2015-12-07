package org.cmas.service.dictionary;

import android.content.Context;
import org.cmas.util.ProgressTask;

public interface DictionaryDataService {

    void loadDictionaryEntities(Context context, ProgressTask.OnPublishProgressListener progressUpdateListener, int maxProgress);
}
