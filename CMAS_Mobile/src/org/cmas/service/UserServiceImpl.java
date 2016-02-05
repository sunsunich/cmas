package org.cmas.service;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.BaseBeanContainer;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.dao.DataBaseHolder;
import org.cmas.dao.UserDao;
import org.cmas.entities.User;
import org.cmas.remote.MockUtil;

public class UserServiceImpl<T extends User>  implements UserService<T>, InitializingBean {

    private UserDao<T> userDao;

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        userDao = (UserDao<T>)beanContainer.getUserDao();
    }

    @Override
    public void persistUser(Context context, T user, boolean isNewUser) {
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase writableDatabase = dataBaseHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
        try {
            writableDatabase.beginTransaction();
            try {
                if (isNewUser) {
                    userDao.save(writableDatabase, user);
                } else {
                    userDao.update(writableDatabase, user);
                }
                writableDatabase.setTransactionSuccessful();
            } finally {
                writableDatabase.endTransaction();
            }
        } finally {
            writableDatabase.close();
        }
    }

    @Override
    public T getByEmail(Context context, String email) {
    //    return (T)MockUtil.getMockInstructor();
//        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
//        SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
//        try {
//            return userDao.getByEmail(readableDatabase, email);
//        } finally {
//            readableDatabase.close();
//        }
        return (T) MockUtil.loginMockDiver(email, "aaa").first;
    }

    @Override
    public T getById(Context context, long id) throws Exception {
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
        try {
            return userDao.getById(readableDatabase, id);
        } finally {
            readableDatabase.close();
        }
    }
}
