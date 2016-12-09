package org.cmas.service.divespot;

import android.content.Context;
import com.google.android.gms.maps.model.LatLngBounds;
import org.cmas.entities.divespot.DiveSpot;

import java.util.List;

/**
 * Created on Jan 06, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiveSpotService {

    DiveSpot addDiveSpot(Context context,DiveSpot diveSpot, boolean isNew);

    void deleteDiveSpot(Context context, DiveSpot diveSpot);

    List<DiveSpot> getInMapBounds(Context context, LatLngBounds bounds);
}
