package org.cmas.service;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.BaseBeanContainer;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.R;
import org.cmas.dao.DataBaseHolder;
import org.cmas.dao.UserDao;
import org.cmas.entities.User;

public class CodeServiceImpl implements CodeService, InitializingBean {

    private UserDao userDao;

    @Override
    public void initialize() {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        userDao = beanContainer.getUserDao();
    }

    @Override
    public String checkCode(Context context, String username, String code) {
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
        try {
            User user = userDao.getByEmail(readableDatabase, username);
            if(user.getMobileLockCode().equals(code.trim())){
                return "";
            }
            else {
                return context.getString(R.string.wrong_code);
            }
        } finally {
            readableDatabase.close();
        }
    }
}
