package org.cmas.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import com.google.android.gcm.GCMRegistrar;
import org.cmas.Settings;
import org.cmas.entities.DeviceType;
import org.cmas.entities.User;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.json.user.RegisterNewUserReply;
import org.cmas.util.android.SecurePreferences;

import java.util.HashMap;
import java.util.Map;

public class RemoteRegistrationServiceImpl extends BaseRemoteServiceImpl implements RemoteRegistrationService {

    @Override
    public Pair<User, String> loginUsername(
            Context context,
            LoginData loginData
    ) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", loginData.username);
        params.put("password", loginData.password);
        params.put("deviceType", DeviceType.ANDROID.name());
        params.put("deviceId", loginData.deviceId);
        params.put("pushServiceRegId", loginData.gcmRegId);

        Pair<Pair<User, String>, Map<String, String>> result =
                basicGetRequestSend(context, appProperties.getLoginURL(), params, User.class);

        User user = result.first.first;
        if (user != null) {
            SharedPreferences sharedPreferences = new SecurePreferences(context);
            Settings settings = settingsService.getSettings(sharedPreferences);

            settings.setJsessionid(result.second.get(SESSION_COOKIE_NAME));
            GCMRegistrar.setRegisteredOnServer(context, true);
            settingsService.setSettings(sharedPreferences, settings);
        }
        return result.first;
    }


    @Override
    public Pair<RegisterNewUserReply, String> registerUsername(Context context, String username, String password) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);

        Pair<Pair<RegisterNewUserReply, String>, Map<String, String>> result =
                basicGetRequestSend(context, appProperties.getRegisterNewUserURL(), params, RegisterNewUserReply.class);
        return result.first;
    }

    @Override
    public Pair<SimpleGsonResponse, String> addCode(Context context, String code)
            throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mobileLockCode", code);

        Pair<Pair<SimpleGsonResponse, String>, Map<String, String>> result =
                basicGetRequestSend(context, appProperties.getAddCodeURL(), params, SimpleGsonResponse.class);
        return result.first;
    }

    @Override
    public Pair<SimpleGsonResponse, String> addEmail(Context context, String email)
            throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", email);

        Pair<Pair<SimpleGsonResponse, String>, Map<String, String>> result =
                basicGetRequestSend(context, appProperties.getAddEmailURL(), params, SimpleGsonResponse.class);
        return result.first;
    }

    @Override
    public Pair<SimpleGsonResponse, String> registerDevice(
            Context context, String deviceId, String gcmRegId)
            throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deviceType", DeviceType.ANDROID.name());
        params.put("deviceId", deviceId);
        params.put("pushServiceRegId", gcmRegId);

        Pair<Pair<SimpleGsonResponse, String>, Map<String, String>> result =
                basicGetRequestSend(context, appProperties.getRegisterDeviceURL(), params, SimpleGsonResponse.class);
        Pair<SimpleGsonResponse, String> responseStringPair = result.first;
        if (responseStringPair.first != null) {
            GCMRegistrar.setRegisteredOnServer(context, true);
        }
        return responseStringPair;
    }

    @Override
    public Pair<SimpleGsonResponse, String> unregisterDevice(Context context, String deviceId, String gcmRegId)
            throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deviceId", deviceId);
        params.put("pushServiceRegId", gcmRegId);

        Pair<Pair<SimpleGsonResponse, String>, Map<String, String>> result =
                basicGetRequestSend(context, appProperties.getUnregisterDeviceURL(), params, SimpleGsonResponse.class);
        return result.first;
    }
}
