package org.cmas.service.dictionary;

import android.util.Pair;
import org.cmas.entities.DictionaryEntity;

import java.util.List;

public interface RemoteListListEntityGetter {

    Pair<List<List<? extends DictionaryEntity>>, String> getEntitiesList(List<Long> maxVersions) throws Exception;
}
