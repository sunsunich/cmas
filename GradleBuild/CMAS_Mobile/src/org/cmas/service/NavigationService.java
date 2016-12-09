package org.cmas.service;

import android.app.Activity;
import android.os.Bundle;
import org.cmas.fragments.SecureFragment;

public interface NavigationService {
    //todo разобраться с отображением Trial окон и после этого здесь
    Class<? extends Activity> getMainActivity();

    Class<? extends Activity> getLoggedOffActivity();

    Class<? extends Activity> getRegistrationActivity();

    Class<? extends SecureFragment> getMainFragmentClass();

    SecureFragment getMainFragment(Bundle data);

}
