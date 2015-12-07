package org.cmas.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import org.cmas.BaseBeanContainer;
import org.cmas.service.LoginService;
import org.cmas.service.NavigationService;
import org.cmas.service.PushDispatcherService;
import org.cmas.util.android.BundleUtil;
import org.cmas.util.android.SecurePreferences;

public class Dispatcher extends Activity {

    public static final String LAST_ACTIVITY_PROPNAME = "Egosanum.lastActivity";
    public static final String LAST_FRAGMENT_PROPNAME = "Egosanum.lastFragment";

    private final BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
    private final PushDispatcherService pushDispatcherService = beanContainer.getPushDispatcherService();
    private final NavigationService navigationService = beanContainer.getNavigationService();
    private final LoginService loginService = beanContainer.getLoginService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        registerReceiver( mHandleMessageReceiver
//                        , new IntentFilter(GCMIntentService.PUSH_MESSAGE_ACTION)
//        );

        dispatchToStoredStateIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Intent newIntent = pushDispatcherService.handlePush(this, intent);
        if (newIntent == null) {
            dispatchToStoredStateIntent();
        } else {
            startActivity(newIntent);
            finish();
        }
    }

    private void dispatchToStoredStateIntent() {
        SharedPreferences sharedPreferences = new SecurePreferences(this);
        Intent intent;
        Class<? > activityClass;
        Class<? extends Activity> loggedOffActivity = navigationService.getLoggedOffActivity();
        try {
            String activityClassName = sharedPreferences.getString(
                    LAST_ACTIVITY_PROPNAME,
                    loggedOffActivity.getName()
            );
            activityClass = Class.forName(activityClassName);
            intent = new Intent(this, activityClass);
            if (!activityClass.equals(AuthorizedHolder.class)) {
                Bundle bundle = new Bundle();
                BundleUtil.loadBundle(sharedPreferences, bundle);
                intent.putExtras(bundle);
            }
        } catch (Exception e) {
            Log.e(getClass().getName()
                    , "Error while loading bundle"
                    , e
            );
            activityClass = loggedOffActivity;
            intent = new Intent(this, activityClass);
        }
        if(activityClass.equals(loggedOffActivity)){
            //TODO add username
            loginService.logout(this, "");
        }

        startActivity(intent);
        finish();
    }

    /**
     * Receiving push messages
     */
//    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String message = intent.getExtras().getString("profileId");
//            // Waking up mobile if it is sleeping
//            wakeLocker.acquire(getApplicationContext());
//            try {
//                /**
//                 * Take appropriate action on this message
//                 * depending upon your app requirement
//                 * For now i am just displaying it on the screen
//                 * */
//
//                handleUserPushResponse(message);
//            } finally {
//                // Releasing wake lock
//                wakeLocker.release();
//            }
//        }
//    };
}
