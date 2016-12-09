package org.cmas.presentation.dao;

import org.cmas.entities.Country;
import org.cmas.entities.Toponym;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public interface ToponymDao extends DictionaryDataDao<Toponym> {

    Toponym getByCountryAndName(String name, Country country);

}
