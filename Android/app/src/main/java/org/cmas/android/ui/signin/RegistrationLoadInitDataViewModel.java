package org.cmas.android.ui.signin;

import android.util.Log;
import org.cmas.android.storage.MobileDatabase;
import org.cmas.android.storage.dao.CountryDao;
import org.cmas.android.storage.dao.NationalFederationDao;
import org.cmas.util.TaskProgressUpdate;
import org.cmas.util.android.TaskViewModel;

import javax.annotation.Nullable;

public class RegistrationLoadInitDataViewModel extends TaskViewModel<Void, TaskProgressUpdate, RegistrationInitData> {

    public int countrySelectionInitPosition = 0;
    public int federationSelectionInitPosition = 0;
    public boolean startSingleItemValidation;

    @Nullable
    @Override
    protected RegistrationInitData runInBackground(Void arg) {
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