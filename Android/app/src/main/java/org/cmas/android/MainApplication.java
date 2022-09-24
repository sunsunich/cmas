package org.cmas.android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.multidex.MultiDexApplication;
import org.cmas.android.ui.signin.PostToServerService;
import org.cmas.ecards.R;

public class MainApplication extends MultiDexApplication {

    private static MainApplication context;

    public static MainApplication getAppContext() {
        return MainApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MainApplication.context = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    PostToServerService.CHANNEL_ID,
                    getString(R.string.upload_progress_header),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
