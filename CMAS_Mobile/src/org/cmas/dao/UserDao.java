package org.cmas.dao;

import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.entities.User;

public interface UserDao<T extends User> extends GeneralDao<T> {

    T getByEmail(SQLiteDatabase database, String username);
}
