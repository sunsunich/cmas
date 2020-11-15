package org.cmas.android.storage.convert;

import org.cmas.android.storage.MobileDatabase;
import org.cmas.android.storage.dao.CountryDao;
import org.cmas.android.storage.entities.Country;

public class CountryPersisterImpl extends DictionaryEntityPersisterImpl<CountryDao, org.cmas.entities.Country, Country> {

    @Override
    protected CountryDao getDao() {
        return MobileDatabase.getInstance().getCountryDao();
    }

    @Override
    protected Class<Country> getEntityClass() {
        return Country.class;
    }
}
