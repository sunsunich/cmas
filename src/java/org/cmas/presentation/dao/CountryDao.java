package org.cmas.presentation.dao;

import org.cmas.entities.Country;

import java.util.List;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public interface CountryDao extends DictionaryDataDao<Country> {

    Country getByCode(String code);

    List<Country> getAll();
}
