package org.cmas.android.ui.file;

import org.cmas.android.MainApplication;
import org.cmas.util.android.StorageUtil;
import org.cmas.util.android.TaskViewModel;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public class CreateImageFileViewModel extends TaskViewModel<Void, Void, File> {

    @Nullable
    @Override
    protected File runInBackground(Void arg) {
        try {
            return StorageUtil.createTmpImageFile(MainApplication.getAppContext());
        } catch (IOException ignored) {
            return null;
        }
    }
}
