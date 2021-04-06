package org.cmas.android.ui.signin;

import android.util.Log;
import org.cmas.android.storage.MobileDatabase;
import org.cmas.android.storage.dao.CountryDao;
import org.cmas.android.storage.dao.NationalFederationDao;
import org.cmas.util.TaskProgressUpdate;
import org.cmas.util.android.TaskViewModel;

public class RegistrationLoadInitDataViewModel extends TaskViewModel<Void, TaskProgressUpdate, RegistrationInitData> {

    @Override
    protected RegistrationInitData runInBackground(Void... args) {
        try {
            MobileDatabase database = MobileDatabase.getInstance();
            CountryDao countryDao = database.getCountryDao();
            NationalFederationDao nationalFederationDao = database.getNationalFederationDao();
            return new RegistrationInitData(
                    countryDao.getAllSortedAlphabetically(),
                    nationalFederationDao.getAllSortedAlphabetically()
            );
        } catch (Throwable e){
            Log.e(getClass().getName(), e.getMessage(), e);
            return null;
        }
    }
}