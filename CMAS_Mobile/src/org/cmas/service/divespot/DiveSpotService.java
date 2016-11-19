package org.cmas.service.divespot;

import android.content.Context;
import org.cmas.entities.divespot.DiveSpot;

//import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created on Jan 06, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiveSpotService {

    DiveSpot addDiveSpot(Context context,DiveSpot diveSpot, boolean isNew);

    void deleteDiveSpot(Context context, DiveSpot diveSpot);

//    List<DiveSpot> getInMapBounds(Context context, LatLngBounds bounds);
}
