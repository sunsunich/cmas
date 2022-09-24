package org.cmas.android.ui.file;

import android.net.Uri;
import android.util.Log;
import org.cmas.ecards.R;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.cmas.android.MainApplication;
import org.cmas.util.android.StorageUtil;
import org.cmas.util.android.TaskViewModel;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class CopyImageFileViewModel extends TaskViewModel<Uri, Void, Pair<Integer, File>> {

    @Nullable
    @Override
    protected Pair<Integer, File> runInBackground(Uri arg) {
        try {
            MainApplication context = MainApplication.getAppContext();
            File tmpImageFile = StorageUtil.createTmpImageFile(context);
            try (InputStream inputStream = context.getContentResolver().openInputStream(arg);
                 FileOutputStream fileOutputStream = new FileOutputStream(tmpImageFile)
            ) {
                IOUtils.copy(inputStream, fileOutputStream);
            }
            if (!StorageUtil.validateFileSize(tmpImageFile.getPath())) {
                tmpImageFile.delete();
                return Pair.of(R.string.error_image_too_big, null);
            }
            return Pair.of(null, tmpImageFile);
        } catch (Exception e) {
            Log.e(getClass().getName(), "error while copying to file from uri: " + arg, e);
            return Pair.of(R.string.error_image_creation, null);
        }
    }
}
