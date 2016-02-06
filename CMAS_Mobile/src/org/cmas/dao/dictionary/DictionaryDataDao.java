package org.cmas.dao.dictionary;

import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.dao.GeneralDao;
import org.cmas.entities.DictionaryEntity;

import java.util.List;

public interface DictionaryDataDao<T extends DictionaryEntity> extends GeneralDao<T> {

    long getMaxVersion(SQLiteDatabase database);

    String getAllColumnsStr();

    String getTableAlias();

    List<T> getAll(SQLiteDatabase database, long version);

    T getByName(SQLiteDatabase database, String name);
}
