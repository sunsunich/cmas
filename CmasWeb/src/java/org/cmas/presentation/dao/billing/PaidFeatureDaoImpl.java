package org.cmas.presentation.dao.billing;

import org.cmas.entities.loyalty.PaidFeature;
import org.cmas.presentation.dao.DictionaryDataDaoImpl;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class PaidFeatureDaoImpl extends DictionaryDataDaoImpl<PaidFeature> implements PaidFeatureDao {

    @Override
    public List<PaidFeature> getAll() {
        //noinspection unchecked
        return createNotDeletedCriteria().addOrder(Order.asc("id")).list();
    }

    @Override
    public List<PaidFeature> getByIds(List<Long> featureIds) {
        //noinspection unchecked
        return createNotDeletedCriteria().add(Restrictions.in("id", featureIds)).list();
    }
}
