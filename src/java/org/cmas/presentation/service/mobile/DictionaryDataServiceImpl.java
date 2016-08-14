package org.cmas.presentation.service.mobile;

import org.cmas.entities.Country;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.user.sport.NationalFederationDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DictionaryDataServiceImpl implements DictionaryDataService {

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private NationalFederationDao nationalFederationDao;

    @Override
    public List<Country> getCountries(long version) throws Exception {
        return countryDao.getAllOlderThanVersion(version);
    }

    @Override
    public List<NationalFederation> getNationalFederations(long version) throws Exception {
        return nationalFederationDao.getAllOlderThanVersion(version);
    }

}
