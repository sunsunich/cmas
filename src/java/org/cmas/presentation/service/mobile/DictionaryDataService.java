package org.cmas.presentation.service.mobile;

import org.cmas.entities.Country;

import java.util.List;

public interface DictionaryDataService {

    List<Country> getCountries(long version) throws Exception;

}
