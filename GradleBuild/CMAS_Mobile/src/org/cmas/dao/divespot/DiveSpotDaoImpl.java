package org.cmas.dao.divespot;

import android.content.ContentValues;
import android.database.Cursor;
import org.cmas.InitializingBean;
import org.cmas.dao.dictionary.DictionaryDataDaoImpl;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.util.StringUtil;

//import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created on Jan 06, 2016
 *
 * @author Alexander Petukhov
 */
public class DiveSpotDaoImpl extends DictionaryDataDaoImpl<DiveSpot> implements DiveSpotDao, InitializingBean {

    public static final String DIVE_SPOT_TABLE = "dive_spots";

    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";


    private static final String CREATE_TABLE_ENDING_QUERY =
            ", " + LONGITUDE + " REAL, "
                    + LATITUDE + " REAL "
                    + ");";

    private static final String[] ADDITIONAL_COLUMNS = {
            LONGITUDE,
            LATITUDE
    };


    @Override
    protected String getTableCreateQueryEnding() {
        return CREATE_TABLE_ENDING_QUERY;
    }

    @Override
    public String[] getAllColumns() {
        return StringUtil.concatArrays(super.getAllColumns(), ADDITIONAL_COLUMNS);
    }


    private String getByBoundsSql;

    @Override
    public void initialize() {
        String dAlias = getTableAlias();
        getByBoundsSql = "select distinct " + getAllColumnsStr()
                + " from " + DIVE_SPOT_TABLE + " as " + dAlias
                + " where " + dAlias + '.' + LONGITUDE + " >= ?"
                + " and " + dAlias + '.' + LONGITUDE + " <= ?"
                + " and " + dAlias + '.' + LATITUDE + " >= ?"
                + " and " + dAlias + '.' + LATITUDE + " <= ?"
                + " and " + dAlias + '.' + COLUMN_DELETED + " = 0"
        ;
    }

    @Override
    public DiveSpot cursorToEntity(Cursor cursor, int index) {
        DiveSpot entity = super.cursorToEntity(cursor, index);
        int i = index + super.getAllColumns().length;
        entity.setLongitude((double) cursor.getFloat(i));
        i++;
        entity.setLatitude((double) cursor.getFloat(i));
        return entity;

    }

    @Override
    protected ContentValues entityToContentValues(DiveSpot entity) {
        ContentValues values = super.entityToContentValues(entity);
        values.put(LONGITUDE, entity.getLongitude());
        values.put(LATITUDE, entity.getLatitude());
        return values;
    }

//    @Override
//    public List<DiveSpot> getInMapBounds(SQLiteDatabase database, LatLngBounds bounds) {
//        try (Cursor cursor = database.rawQuery(
//                getByBoundsSql,
//                new String[]{
//                        String.valueOf(bounds.southwest.longitude),
//                        String.valueOf(bounds.northeast.longitude),
//                        String.valueOf(bounds.southwest.latitude),
//                        String.valueOf(bounds.northeast.latitude)
//                }
//
//        )) {
//            List<DiveSpot> result = new ArrayList<>(cursor.getCount());
//            if (cursor.moveToFirst()) {
//                do {
//                    DiveSpot logbookEntry = cursorToEntity(cursor, 0);
//                    result.add(logbookEntry);
//                } while (cursor.moveToNext());
//            }
//
//            return result;
//        }
//    }

    @Override
    protected DiveSpot constructEntity() {
        return new DiveSpot();
    }

    @Override
    public String getTableAlias() {
        return "ds";
    }

    @Override
    public String getTableName() {
        return DIVE_SPOT_TABLE;
    }
}
