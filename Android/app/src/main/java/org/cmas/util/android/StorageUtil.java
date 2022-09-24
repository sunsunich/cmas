package org.cmas.util.android;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import org.cmas.Globals;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class StorageUtil {

    private StorageUtil() {
    }

    public static File createTmpImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            Log.e(StorageUtil.class.getName(), "error while creating file: " + imageFileName, e);
            throw e;
        }
    }

    public static boolean validateFileSize(String path) {
        return new File(path).length() < Globals.MAX_IMAGE_SIZE;
    }
}
