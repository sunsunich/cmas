package org.cmas.android.ui.signin;

import org.cmas.util.TaskProgressUpdate;
import org.cmas.util.android.TaskViewModel;

public class RegistrationViewModel extends TaskViewModel<RegistrationFormObject, TaskProgressUpdate, Boolean> {

    @Override
    protected Boolean runInBackground(RegistrationFormObject... args) {
       return false;
    }

}
