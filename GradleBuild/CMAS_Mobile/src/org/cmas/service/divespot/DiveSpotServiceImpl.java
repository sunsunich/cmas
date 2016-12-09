package org.cmas.service.divespot;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.BaseBeanContainer;
import org.cmas.Globals;
import org.cmas.InitializingBean;
import org.cmas.dao.DataBaseHolder;
import org.cmas.dao.divespot.DiveSpotDao;
import org.cmas.entities.divespot.DiveSpot;

//import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created on Jan 06, 2016
 *
 * @author Alexander Petukhov
 */
public class DiveSpotServiceImpl implements DiveSpotService, InitializingBean{

    private DiveSpotDao diveSpotDao;

    @Override
    public void initialize() {
        diveSpotDao = BaseBeanContainer.getInstance().getDiveSpotDao();
    }

//    @Override
//    public List<DiveSpot> getInMapBounds(Context context, LatLngBounds bounds) {
//        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
//        SQLiteDatabase readableDatabase = dataBaseHolder.getReadableDatabase(Globals.MOBILE_DB_PASS);
//        try {
//            return diveSpotDao.getInMapBounds(readableDatabase, bounds);
//        } finally {
//            readableDatabase.close();
//        }
//    }

    private static long id = 1L;

    @Override
    public DiveSpot addDiveSpot(Context context, DiveSpot diveSpot, boolean isNew) {
        //todo remote call
        if(isNew) {
            diveSpot.setId(id++);
        }
        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase writableDatabase = dataBaseHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
        try {
            if(isNew) {
                diveSpotDao.save(writableDatabase, diveSpot);
            }
            else{
                diveSpotDao.update(writableDatabase, diveSpot);
            }
        } finally {
            writableDatabase.close();
        }

        return diveSpot;
    }

    @Override
    public void deleteDiveSpot(Context context, DiveSpot diveSpot) {
        //todo remote call

        DataBaseHolder dataBaseHolder = new DataBaseHolder(context);
        SQLiteDatabase writableDatabase = dataBaseHolder.getWritableDatabase(Globals.MOBILE_DB_PASS);
        try {
            diveSpotDao.delete(writableDatabase, diveSpot);
        } finally {
            writableDatabase.close();
        }
    }
}
