package org.cmas.presentation.dao.user.sport;

import org.cmas.entities.Country;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.DictionaryDataDao;

import java.util.List;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public interface NationalFederationDao extends DictionaryDataDao<NationalFederation> {

    //todo remove?
    NationalFederation getFederationForSportsman(String firstName, String lastName, Country country);

    NationalFederation getByCountry(Country country);

    List<NationalFederation> getAll();
}
