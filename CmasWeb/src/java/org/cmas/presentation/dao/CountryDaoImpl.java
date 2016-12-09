package org.cmas.presentation.dao;

import org.cmas.entities.Country;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class CountryDaoImpl extends DictionaryDataDaoImpl<Country> implements CountryDao {

    @Override
    public Country getByCode(String code) {
        return (Country)createNotDeletedCriteria().add(Restrictions.eq("code", code)).uniqueResult();
    }

    @Override
    public List<Country> getAll() {
        //noinspection unchecked
        return createNotDeletedCriteria().addOrder(Order.asc("name")).list();
    }
}
