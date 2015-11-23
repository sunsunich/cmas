package org.cmas.presentation.dao;

import org.cmas.entities.Country;
import org.cmas.util.dao.HibernateDaoImpl;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class CountryDaoImpl extends HibernateDaoImpl<Country> implements CountryDao {

    @Override
    public Country getByName(String name) {
        return (Country)createCriteria().add(Restrictions.eq("name", name)).uniqueResult();
    }

    @Override
    public List<Country> getAll() {
        //noinspection unchecked
        return createCriteria().list();
    }
}
