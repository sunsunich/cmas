package org.cmas.presentation.service.mobile;

import org.cmas.entities.Country;
import org.cmas.presentation.dao.CountryDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DictionaryDataServiceImpl implements DictionaryDataService {

    @Autowired
    private CountryDao countryDao;

    @Override
    public List<Country> getCountries(long version) throws Exception {
        return countryDao.getAllOlderThanVersion(version);
    }

}
