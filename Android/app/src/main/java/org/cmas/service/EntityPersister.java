package org.cmas.service;

import javax.annotation.Nullable;
import java.util.Collection;

public interface EntityPersister<C> {

    void persist(@Nullable C container);

    void persist(@Nullable Collection<C> containerCollection);
}
