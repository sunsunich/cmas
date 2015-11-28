package org.cmas.presentation.service.mobile;

import org.cmas.entities.Country;
import org.cmas.entities.Role;

import java.util.List;

public interface DictionaryDataService {

    List<Country> getCountries(long version) throws Exception;

    List<Role> getRoles(long version) throws Exception;
}
