package org.cmas.android.ui.verify;

import org.cmas.util.TaskProgressUpdate;
import org.cmas.util.android.TaskViewModel;

import javax.annotation.Nullable;

public class DiverVerificationViewModel extends TaskViewModel<DiverVerificationFormObject, TaskProgressUpdate, Boolean> {

    @Nullable
    @Override
    protected Boolean runInBackground(DiverVerificationFormObject arg) {
       return false;
    }


}
