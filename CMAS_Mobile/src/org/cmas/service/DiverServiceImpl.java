package org.cmas.service;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.BaseBeanContainer;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.dao.DataBaseHolder;
import org.cmas.dao.DiverDao;
import org.cmas.entities.diver.Diver;

public class DiverServiceImpl implements DiverService, InitializingBean {

    private DiverDao diverDao;

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        diverDao = beanContainer.getDiverDao();
    }

    @Override
    public void persist(Context context, Diver user, boolean isNewUser) {
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase writableDatabase = dataBaseHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
        try {
            writableDatabase.beginTransaction();
            try {
                if (isNewUser) {
                    diverDao.save(writableDatabase, user);
                } else {
                    diverDao.update(writableDatabase, user);
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
    public Diver getByEmail(Context context, String email) {
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
        try {
            return diverDao.getByEmail(readableDatabase, email);
        } finally {
            readableDatabase.close();
        }
//        return (T) MockUtil.loginMockDiver(email, "aaa").first;
    }

    @Override
    public Diver getById(Context context, long id) throws Exception {
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
        try {
            return diverDao.getById(readableDatabase, id);
        } finally {
            readableDatabase.close();
        }
    }
}
