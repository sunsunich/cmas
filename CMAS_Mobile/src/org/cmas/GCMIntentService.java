package org.cmas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.remote.RemoteRegistrationService;
import org.cmas.service.LoginService;
import org.cmas.service.ReLoginAction;
import org.cmas.util.android.SecurePreferences;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 16/11/12
 * Time: 14:21
 */
public class GCMIntentService {//} extends GCMBaseIntentService {

//    public static final String PUSH_MESSAGE_ACTION =
//                "com.aegosanum.PUSH_MESSAGE";
    public static final String EGOSANUM_PUSH_PROFILE = "cmas.push.user";

    public GCMIntentService(){
     //   super(BaseBeanContainer.getInstance().getAppProperties().getGcmSenderId());
    }

    public GCMIntentService(String senderId) {
 //       super(senderId);
    }

//    /**
//     * Notifies UI about push
//     */
//    private static void sendPushBroadcast(Context context, String message) {
//        Intent intent = new Intent(PUSH_MESSAGE_ACTION);
//        intent.putExtra("profileId", message);
//        context.sendBroadcast(intent);
//    }

  //  @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(getClass().getName(), "Received message");

        BaseBeanContainer.getInstance().getPushDispatcherService().generateNotification(
                context, intent
        );
      //  sendPushBroadcast(context, message);
    }

//    @Override
    protected void onError(Context context, String s) {
        Log.i(getClass().getName(), "Got onError ");
        Log.e(getClass().getName(), "Error: " + s);
    }

  //  @Override
    protected void onRegistered(final Context context, final String registrationId) {
        Log.i(getClass().getName(), "Device registered: regId = " + registrationId);
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        SharedPreferences sharedPreferences = new SecurePreferences(context);
        SettingsService settingsService = beanContainer.getSettingsService();
        final Settings settings = settingsService.getSettings(sharedPreferences);
        settings.setGcmRegistrationId(registrationId);
        settingsService.setSettings(sharedPreferences, settings);

        LoginService loginService = beanContainer.getLoginService();
        final RemoteRegistrationService remoteRegistrationService = beanContainer.getRemoteRegistrationService();

        ReLoginAction<SimpleGsonResponse> reLoginAction = new ReLoginAction<SimpleGsonResponse>(loginService) {
            @Override
            protected Pair<SimpleGsonResponse, String> getRemoteResult() throws Exception {
                return remoteRegistrationService.registerDevice(
                        context, settings.getDeviceId(), registrationId
                );
            }

            @Override
            protected Pair<SimpleGsonResponse, String> nullResultHandler(String errorMessage) {
                return new Pair<SimpleGsonResponse, String>(null, errorMessage);
            }

            @Override
            protected void okResultHandler(SimpleGsonResponse okResult) {
            }
        };
        reLoginAction.doAction(context);
    }

 //   @Override
    protected void onUnregistered(final Context context, final String registrationId) {
        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        SharedPreferences sharedPreferences = new SecurePreferences(context);
        SettingsService settingsService = beanContainer.getSettingsService();
        final Settings settings = settingsService.getSettings(sharedPreferences);
        LoginService loginService = beanContainer.getLoginService();
        final RemoteRegistrationService remoteRegistrationService = beanContainer.getRemoteRegistrationService();

        ReLoginAction<SimpleGsonResponse> reLoginAction = new ReLoginAction<SimpleGsonResponse>(loginService) {
            @Override
            protected Pair<SimpleGsonResponse, String> getRemoteResult() throws Exception {
                return remoteRegistrationService.unregisterDevice(
                        context, settings.getDeviceId(), registrationId
                );
            }

            @Override
            protected Pair<SimpleGsonResponse, String> nullResultHandler(String errorMessage) {
                return new Pair<SimpleGsonResponse, String>(null, errorMessage);
            }

            @Override
            protected void okResultHandler(SimpleGsonResponse okResult) {
            }
        };
        reLoginAction.doAction(context);
    }
}
