package org.cmas.presentation.dao.divespot;

import org.cmas.entities.divespot.DiveSpot;
import org.cmas.presentation.dao.DictionaryDataDaoImpl;
import org.cmas.presentation.model.divespot.LatLngBounds;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created on Jan 06, 2016
 *
 * @author Alexander Petukhov
 */
public class DiveSpotDaoImpl extends DictionaryDataDaoImpl<DiveSpot> implements DiveSpotDao {

    @Override
    public List<DiveSpot> getInMapBounds(LatLngBounds bounds) {
        //noinspection unchecked
        return createNotDeletedCriteria()
                .add(Restrictions.ge("longitude", bounds.getSwLongitude()))
                .add(Restrictions.le("longitude", bounds.getNeLongitude()))
                .add(Restrictions.ge("latitude", bounds.getSwLatitude()))
                .add(Restrictions.le("latitude", bounds.getNeLatitude()))
                .list();
    }
}
