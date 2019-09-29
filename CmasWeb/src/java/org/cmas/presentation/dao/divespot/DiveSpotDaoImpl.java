package org.cmas.presentation.dao.divespot;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.presentation.dao.DictionaryDataDaoImpl;
import org.cmas.presentation.model.divespot.LatLngBounds;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on Jan 06, 2016
 *
 * @author Alexander Petukhov
 */
public class DiveSpotDaoImpl extends DictionaryDataDaoImpl<DiveSpot> implements DiveSpotDao {

    @Override
    public List<DiveSpot> getInMapBounds(LatLngBounds bounds, Diver diver, Boolean isApproved) {
        Set<Long> nonEditableSpotIds = null;
        DiverRegistrationStatus diverRegistrationStatus = diver.getDiverRegistrationStatus();
        if (diverRegistrationStatus == DiverRegistrationStatus.GUEST
            || diverRegistrationStatus == DiverRegistrationStatus.CMAS_FULL) {
            nonEditableSpotIds = getNonEditableSpotIds(diver);
        }

        Criteria criteria = createNotDeletedCriteria();
        if (isApproved != null) {
            criteria.add(Restrictions.eq("isApproved", isApproved));
        }
        //noinspection unchecked
        List<DiveSpot> diveSpots = criteria
                .add(Restrictions.ge("longitude", bounds.getSwLongitude()))
                .add(Restrictions.le("longitude", bounds.getNeLongitude()))
                .add(Restrictions.ge("latitude", bounds.getSwLatitude()))
                .add(Restrictions.le("latitude", bounds.getNeLatitude()))
                .list();
        for (DiveSpot diveSpot : diveSpots) {
            diveSpot.setEditable(
                    isSpotEditable(nonEditableSpotIds, diveSpot, diver)
            );
        }
        return diveSpots;
    }

    private boolean isSpotEditable(Set<Long> nonEditableSpotIds, DiveSpot diveSpot, Diver diver) {
        return nonEditableSpotIds != null
               && !diveSpot.isApproved()
               && !nonEditableSpotIds.contains(diveSpot.getId())
               && (diveSpot.getCreator() == null || diveSpot.getCreator().getId() == diver.getId())
                ;
    }

    @Override
    public boolean isSpotEditable(DiveSpot diveSpot, Diver diver) {
        return isSpotEditable(getNonEditableSpotIds(diver), diveSpot, diver);
    }

    private Set<Long> getNonEditableSpotIds(Diver diver) {
        String hql = "select distinct ds.id from org.cmas.entities.logbook.LogbookEntry le"
                     + " inner join le.diveSpot ds"
                     + " inner join le.diver d"
                     + " where d.id != :diverId";
        return new HashSet<Long>(createQuery(hql).setLong("diverId", diver.getId()).list());
    }
}
