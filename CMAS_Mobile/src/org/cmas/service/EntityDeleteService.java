package org.cmas.service;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.entities.logbook.LogbookEntry;

public interface EntityDeleteService {

    void deleteLogbookEntry(Context context, SQLiteDatabase database, LogbookEntry logbookEntry);

    void deleteAllData(Context context);

}
