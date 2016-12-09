package org.cmas;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.remote.RemoteRegistrationService;
import org.cmas.service.LoginService;
import org.cmas.service.ReLoginAction;
import org.cmas.util.android.SecurePreferences;

/**
 * Created on Oct 08, 2016
 *
 * @author Alexander Petukhov
 */
public class PushInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = PushInstanceIdService.class.getName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        Log.i(getClass().getName(), "Device registered: token = " + token);
        final Context context = this;

        BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
        SharedPreferences sharedPreferences = new SecurePreferences(context);
        SettingsService settingsService = beanContainer.getSettingsService();
        final Settings settings = settingsService.getSettings(sharedPreferences);
        settings.setGcmRegistrationId(token);
        settingsService.setSettings(sharedPreferences, settings);

        LoginService loginService = beanContainer.getLoginService();
        final RemoteRegistrationService remoteRegistrationService = beanContainer.getRemoteRegistrationService();

        ReLoginAction<SimpleGsonResponse> reLoginAction = new ReLoginAction<SimpleGsonResponse>(loginService) {
            @Override
            protected Pair<SimpleGsonResponse, String> getRemoteResult() throws Exception {
                return remoteRegistrationService.registerDevice(
                        context, settings.getDeviceId(), token
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
