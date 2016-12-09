package org.cmas.service.dictionary;

import net.sqlcipher.database.SQLiteDatabase;

public interface EntityPersister<C> {

    void persistEntitiesInContainer(SQLiteDatabase writableDatabase, C container);
}
