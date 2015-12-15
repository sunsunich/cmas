package org.cmas.util.android;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public final class FileUtils {

    private FileUtils() {
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};

            try {
                Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int columnIndex = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex);
                }
            } catch (Exception ignored) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String[] childrenFileNamesFromAssets(Context context, String path) {
        Resources res = context.getResources();
        @SuppressWarnings("resource")
        AssetManager am = res.getAssets();
        String[] fileList = null;
        try {
            fileList = am.list(path);
        } catch (IOException e) {
            Log.d(context.getClass().getName(), e.getLocalizedMessage());
        }
        return fileList;
    }

    public static Bitmap getBitmapFromAsset(Context context, String strName) {
        @SuppressWarnings("resource")
        AssetManager assetManager = context.getAssets();
        try {
            try (InputStream istr = assetManager.open(strName)) {
                return BitmapFactory.decodeStream(istr);
            }
        } catch (IOException e) {
            return null;
        }
    }
}
