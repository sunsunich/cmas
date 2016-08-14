package org.cmas.presentation.service.mobile;

import org.cmas.entities.Country;
import org.cmas.entities.sport.NationalFederation;

import java.util.List;

public interface DictionaryDataService {

    List<Country> getCountries(long version) throws Exception;

    List<NationalFederation> getNationalFederations(long version) throws Exception;
}
