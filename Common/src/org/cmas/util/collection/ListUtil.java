package org.cmas.util.collection;

import java.util.Collection;

public final class ListUtil {

    private ListUtil() {
    }

    public static boolean contains(Collection collection, Object o) {
        return collection.contains(o);
    }
}
