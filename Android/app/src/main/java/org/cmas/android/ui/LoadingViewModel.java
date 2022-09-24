package org.cmas.android.ui;

import android.content.Context;
import android.util.Log;
import org.cmas.ecards.R;
import org.cmas.BaseBeanContainer;
import org.cmas.android.MainApplication;
import org.cmas.android.SystemInitializer;
import org.cmas.remote.NetworkUnavailableException;
import org.cmas.util.TaskProgressUpdate;
import org.cmas.util.android.TaskViewModel;

import javax.annotation.Nullable;

public class LoadingViewModel extends TaskViewModel<Void, TaskProgressUpdate, Boolean> {

    @Nullable
    @Override
    protected Boolean runInBackground(Void arg) {
        Context context = MainApplication.getAppContext();
        try {
            reportProgress(new TaskProgressUpdate(0, context.getString(R.string.initializing_application)));
            SystemInitializer.getInstance().initialize();
            BaseBeanContainer.getInstance().getDictionaryDataService().loadDictionaryEntities(
                    this::translateProgress, 20, 100
            );
            return true;
            // todo different errors on different stage
        } catch (NetworkUnavailableException e) {
            reportProgress(TaskProgressUpdate.getErrorWithNoProgressInstance(
                    context.getString(R.string.no_network_error)
            ));
            return false;
        } catch (Throwable e) {
            Log.e(getClass().getName(), e.getMessage(), e);
            reportProgress(TaskProgressUpdate.getErrorWithNoProgressInstance(
                    context.getString(R.string.fatal_error)
            ));
            return false;
        }
    }

    private void translateProgress(TaskProgressUpdate progressUpdate) {
        Context context = MainApplication.getAppContext();
        String template = context.getString(
                context.getResources()
                       .getIdentifier(progressUpdate.getCurrentStep(), "string", context.getPackageName()),
                new Object[0]
        );
        reportProgress(
                new TaskProgressUpdate(progressUpdate.getProgress(),
                                       template,
                                       progressUpdate.getExpectedDurationMilliSec(),
                                       progressUpdate.isErrorDetected()
                )
        );
    }
}
