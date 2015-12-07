package org.cmas.service;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.entities.doc.Document;

public interface EntityDeleteService {

    void deleteDocument(Context context, SQLiteDatabase database, Document document);

    void deleteAllData(Context context);

}
