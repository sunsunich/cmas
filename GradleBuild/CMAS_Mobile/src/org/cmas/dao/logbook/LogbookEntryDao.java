package org.cmas.dao.logbook;

import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.dao.dictionary.DictionaryDataDao;
import org.cmas.entities.logbook.LogbookEntry;

import java.util.List;

public interface LogbookEntryDao extends DictionaryDataDao<LogbookEntry> {

    List<LogbookEntry> getByUser(SQLiteDatabase database, long diverId, String name);
}
