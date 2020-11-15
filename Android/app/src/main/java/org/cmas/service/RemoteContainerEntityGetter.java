package org.cmas.service;

import org.apache.commons.lang3.tuple.Pair;

public interface RemoteContainerEntityGetter<T> {

    Pair<T, String> getEntityContainer(long maxVersion) throws Exception;
}
