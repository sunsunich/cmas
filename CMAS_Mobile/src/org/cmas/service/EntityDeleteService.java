package org.cmas.service;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.entities.doc.Document;
import org.cmas.entities.logbook.LogbookEntry;

public interface EntityDeleteService {

    void deleteDocument(Context context, SQLiteDatabase database, Document document);

    void deleteLogbookEntry(Context context, SQLiteDatabase database, LogbookEntry logbookEntry);

    void deleteAllData(Context context);

}
