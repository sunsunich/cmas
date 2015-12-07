package org.cmas.dao.doc;

import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.dao.dictionary.DictionaryDataDao;
import org.cmas.entities.doc.Document;

import java.util.List;

public interface DocumentDao extends DictionaryDataDao<Document> {

    List<Document> getByUser(SQLiteDatabase database, long profileId, String name);
}
