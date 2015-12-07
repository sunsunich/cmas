package org.cmas.dao.dictionary;

import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.entities.doc.DocumentType;

import java.util.List;

public interface DocumentTypeDao extends DictionaryDataDao<DocumentType> {

    List<DocumentType> getAllNoDeleted(SQLiteDatabase database);
}
