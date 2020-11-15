package org.cmas.android.storage.convert;

public interface ModelConverter<S, D> {

    D convertFromSource(Class<D> destClass, S sourceModel);

    S convertFromDest(Class<S> sourceClass, D destModel);
}
