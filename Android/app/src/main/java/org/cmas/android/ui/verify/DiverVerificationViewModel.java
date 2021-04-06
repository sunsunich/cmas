package org.cmas.android.ui.verify;

import org.cmas.util.TaskProgressUpdate;
import org.cmas.util.android.TaskViewModel;

public class DiverVerificationViewModel extends TaskViewModel<DiverVerificationFormObject, TaskProgressUpdate, Boolean> {

    @Override
    protected Boolean runInBackground(DiverVerificationFormObject... args) {
       return false;
    }


}
