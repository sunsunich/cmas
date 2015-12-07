package org.cmas.util.android;

import android.os.Environment;

public class DeviceInfo {

    private DeviceInfo(){}

    public static boolean isExternalStorageWritable(){
        boolean isExternalStorageAvailable = false;
        boolean isExternalStorageWritable;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            isExternalStorageAvailable = isExternalStorageWritable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            isExternalStorageAvailable = true;
            isExternalStorageWritable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            isExternalStorageAvailable = isExternalStorageWritable = false;
        }

        return isExternalStorageWritable;
    }
}
