package org.cmas.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.cmas.BaseBeanContainer;
import org.cmas.InitializingBean;
import org.cmas.mobile.R;
import org.cmas.SettingsService;
import org.cmas.activities.AuthorizedHolder;

public class PushDispatcherServiceImpl implements PushDispatcherService, InitializingBean {

    private SettingsService settingsService;
    private NavigationService navigationService;
    private LoginService loginService;

    @Override
    public void initialize() {
        BaseBeanContainer baseBeanContainer = BaseBeanContainer.getInstance();

        settingsService = baseBeanContainer.getSettingsService();
        navigationService = baseBeanContainer.getNavigationService();
        loginService = baseBeanContainer.getLoginService();
    }

    @Override
    public Intent handlePush(Activity activity, Intent intent) {
        return null;
    }

    @Override
    public void generateNotification(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        PushData pushData = new PushData();
        pushData.title = bundle.getString("title", null);
        pushData.subtitle = bundle.getString("subtitle", null);
        pushData.message = bundle.getString("message", null);
        pushData.tickerText = bundle.getString("tickerText", null);
        pushData.allData = bundle.getString("all_data", null);
        // Play default notification sound
        pushData.isMakeSound = "true".equals(bundle.getString("sound", "false"));
        //bundle.getBoolean("vibrate",false) не срабатывает. он все данные как string получает
        pushData.isVibrate = "true".equals(bundle.getString("vibrate", "false"));
        generateNotification(context, pushData);
    }

    @Override
    public void generateNotification(Context context, PushData pushData) {
        try {
            String title = pushData.title == null ? context.getString(R.string.app_name) : pushData.title;

            /*long profileId =Long.parseLong(intent.getExtras().getString("profileId"));
            Profile user = profileService.getById(context, profileId);*/

            long when = System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            int icon = R.drawable.icon;
            Intent notificationIntent = new Intent(context, AuthorizedHolder.class);
            //notificationIntent.putExtra(PushMessagingService.CMAS_PUSH_USER, user);
            // set intent so it does not start a new activity
            //не обязательно
            notificationIntent.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            );
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 0, notificationIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Notification notification = builder.setContentIntent(pendingIntent)
                                               .setSmallIcon(icon)
                                               .setTicker(pushData.tickerText)
                                               .setWhen(when)
                                               .setAutoCancel(true)
                                               .setContentTitle(title)
                                               .setContentInfo(pushData.message)
                                               .setContentText(pushData.subtitle)
                                               .build();

            //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
            // Play default notification sound
            if (pushData.isMakeSound) {
                notification.defaults |= Notification.DEFAULT_SOUND;
            }
            if (pushData.isVibrate) {
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            }

            // Vibrate if vibrate is enabled
            //        notification.defaults |= Notification.DEFAULT_VIBRATE;
            notificationManager.notify(0, notification);
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage(), e);
        }
    }
}
