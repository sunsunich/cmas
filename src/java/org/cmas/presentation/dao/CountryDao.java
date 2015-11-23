package org.cmas.presentation.dao;

import org.cmas.entities.Country;
import org.cmas.util.dao.HibernateDao;

import java.util.List;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public interface CountryDao extends HibernateDao<Country> {

    Country getByName(String name);

    List<Country> getAll();
}
