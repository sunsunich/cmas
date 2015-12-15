package org.cmas.service;

import android.app.Activity;
import android.os.Bundle;
import org.cmas.activities.AuthorizedHolder;
import org.cmas.activities.enter.EnterUsername;
import org.cmas.activities.enter.Registration;
import org.cmas.fragments.FirstPaymentScreen;
import org.cmas.fragments.SecureFragment;

public class NavigationServiceImpl implements NavigationService {

    @Override
    public Class<AuthorizedHolder> getMainActivity() {
        return AuthorizedHolder.class;
    }

    @Override
    public Class<EnterUsername> getLoggedOffActivity() {
        return EnterUsername.class;
    }

    @Override
    public Class<? extends Activity> getRegistrationActivity() {
        return Registration.class;
    }

    @Override
    public Class<? extends SecureFragment> getMainFragmentClass() {
        return FirstPaymentScreen.class;
    }

    @Override
    public SecureFragment getMainFragment(Bundle data) {
        return new FirstPaymentScreen();
    }

}
