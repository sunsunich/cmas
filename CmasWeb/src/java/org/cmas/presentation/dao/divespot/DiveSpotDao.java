package org.cmas.presentation.dao.divespot;


import org.cmas.entities.diver.Diver;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.presentation.model.divespot.LatLngBounds;
import org.cmas.util.dao.HibernateDao;

import java.util.List;

/**
 * Created on Jan 06, 2016
 *
 * @author Alexander Petukhov
 */
public interface DiveSpotDao extends HibernateDao<DiveSpot> {

    List<DiveSpot> getInMapBounds(LatLngBounds bounds, Diver diver, Boolean isApproved);

    boolean isSpotEditable(DiveSpot diveSpot, Diver diver);
}
