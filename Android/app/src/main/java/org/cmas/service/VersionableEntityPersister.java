package org.cmas.service;

public interface VersionableEntityPersister<C> extends EntityPersister<C> {

    long getMaxVersion();
}
