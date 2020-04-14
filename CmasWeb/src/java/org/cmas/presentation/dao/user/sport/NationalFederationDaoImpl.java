package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.Country;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.DictionaryDataDaoImpl;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created on Nov 24, 2015
 *
 * @author Alexander Petukhov
 */
public class NationalFederationDaoImpl extends DictionaryDataDaoImpl<NationalFederation> implements NationalFederationDao {

    @Override
    public List<NationalFederation> getByCountry(Country country) {
        //noinspection unchecked
        return createNotDeletedCriteria().add(Restrictions.eq("country", country)).list();
    }

    @Override
    public List<NationalFederation> getAll() {
        //noinspection unchecked
        return createCriteria().createAlias("country", "c").addOrder(Order.asc("c.name")).list();
    }
}
