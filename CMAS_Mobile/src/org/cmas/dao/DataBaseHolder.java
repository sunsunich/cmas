package org.cmas.dao;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import org.cmas.BaseBeanContainer;
import org.cmas.util.android.DeviceInfo;

import java.io.File;
import java.util.List;

public class DataBaseHolder extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cmas.db";
    private static final int DATABASE_VERSION = 58;

    public static String getDBLocation(Context context) {
        if (DeviceInfo.isExternalStorageWritable()) {
            return context.getExternalFilesDir(null).getAbsolutePath()
                    ;
        } else {
            return context.getFilesDir().getAbsolutePath();
        }
    }

    public static String getDBFullPath(Context context) {
        return getDBLocation(context) + File.separator +
               DATABASE_NAME;
    }

    public DataBaseHolder(Context context) {
        super(context, getDBFullPath(context), null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        List<CreateTableDao> allDaos = BaseBeanContainer.getInstance().getAllDaos();
        for (CreateTableDao dao : allDaos) {
            dao.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        List<CreateTableDao> allDaos = BaseBeanContainer.getInstance().getAllDaos();
        for (CreateTableDao dao : allDaos) {
            dao.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
