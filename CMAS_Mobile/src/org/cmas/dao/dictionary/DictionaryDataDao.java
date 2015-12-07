package org.cmas.dao.dictionary;

import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.dao.GeneralDao;
import org.cmas.entities.DictionaryEntity;

public interface DictionaryDataDao<T extends DictionaryEntity> extends GeneralDao<T> {

    long getMaxVersion(SQLiteDatabase database);

    String getAllColumnsStr();

    String getTableAlias();
}
