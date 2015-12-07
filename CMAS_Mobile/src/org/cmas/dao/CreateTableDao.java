package org.cmas.dao;

import net.sqlcipher.database.SQLiteDatabase;

public interface CreateTableDao {

    void onCreate(SQLiteDatabase database);

    void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion);
}
