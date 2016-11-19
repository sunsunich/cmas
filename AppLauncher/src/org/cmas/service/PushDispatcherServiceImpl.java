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
import org.cmas.SettingsService;
import org.cmas.activities.AuthorizedHolder;
import org.cmas.mobile.R;

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
        Bundle bundle=intent.getExtras();
        try {
            String title=bundle.getString("title",context.getString(R.string.app_name));
            String subtitle=bundle.getString("subtitle",null);
            String message=bundle.getString("message",null);
            /*else {
                message=context.getString(R.string.new_data_for_profile) + ' ' + user.getName();
            }*/
            String tickerText=bundle.getString("tickerText",null);

            //all_data:{json}
            String allData=bundle.getString("all_data",null);

            /*long profileId =Long.parseLong(intent.getExtras().getString("profileId"));
            Profile user = profileService.getById(context, profileId);*/

            long when = System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            int icon = R.drawable.icon;
            Intent notificationIntent = new Intent(context, AuthorizedHolder.class);
            //notificationIntent.putExtra(GCMIntentService.EGOSANUM_PUSH_PROFILE, user);
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
                    .setTicker(tickerText)
                    .setWhen(when)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentInfo(message)
                    .setContentText(subtitle)
                    .build();

            //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
            // Play default notification sound
            if("true".equals(bundle.getString("sound","false")))
            {
                notification.defaults |= Notification.DEFAULT_SOUND;
            }
            //bundle.getBoolean("vibrate",false) не срабатывает. он все данные как string получает
            if("true".equals(bundle.getString("vibrate","false")))
            {
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
