package org.cmas.service.dictionary;

import android.util.Pair;

public interface RemoteContainerEntityGetter<T> {

    Pair<T, String> getEntityContainer(long maxVersion) throws Exception;
}
