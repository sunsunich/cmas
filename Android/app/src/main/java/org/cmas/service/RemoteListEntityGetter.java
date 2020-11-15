package org.cmas.service;

import org.apache.commons.lang3.tuple.Pair;
import org.cmas.entities.DictionaryEntity;

import java.util.List;

public interface RemoteListEntityGetter<T extends DictionaryEntity> {

    Pair<List<T>, String> getEntitiesList(long maxVersion) throws Exception;
}
