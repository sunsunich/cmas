package org.cmas.dao.divespot;

import com.google.android.gms.maps.model.LatLngBounds;
import net.sqlcipher.database.SQLiteDatabase;
import org.cmas.dao.dictionary.DictionaryDataDao;
import org.cmas.entities.divespot.DiveSpot;

import java.util.List;

/**
 * Created on Jan 06, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiveSpotDao extends DictionaryDataDao<DiveSpot> {

    List<DiveSpot> getInMapBounds(SQLiteDatabase database, LatLngBounds bounds);
}
