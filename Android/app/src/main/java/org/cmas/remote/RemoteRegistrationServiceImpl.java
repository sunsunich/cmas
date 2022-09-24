package org.cmas.remote;

import com.google.myjson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.cmas.Globals;
import org.cmas.android.ui.signin.RegistrationFormObject;
import org.cmas.entities.DeviceType;
import org.cmas.json.CommonGsonCreator;
import org.cmas.json.SimpleGsonResponse;

import java.util.HashMap;
import java.util.Map;

public class RemoteRegistrationServiceImpl extends BaseRemoteServiceImpl implements RemoteRegistrationService {

//    @Override
//    public Pair<Diver, String> login(
//            LoginData loginData
//    ) throws Exception {
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("username", loginData.username);
//        params.put("password", loginData.password);
//        params.put("deviceType", DeviceType.ANDROID.name());
//        params.put("deviceId", loginData.deviceId);
//        params.put("pushServiceRegId", loginData.gcmRegId);
//
//        Pair<Pair<Diver, String>, Map<String, String>> result =
//                basicGetRequestSend( appProperties.getLoginURL(), params, Diver.class);
//
//        Diver diver = result.first.first;
//        if (diver != null) {
//            SharedPreferences sharedPreferences = new SecurePreferences(context);
//            Settings settings = settingsService.getSettings(sharedPreferences);
//
//            settings.setJsessionid(result.second.get(SESSION_COOKIE_NAME));
//            //    GCMRegistrar.setRegisteredOnServer( true);
//            settingsService.setSettings(sharedPreferences, settings);
//        }
//        return result.first;
//    }


    @Override
    public Pair<SimpleGsonResponse, String> diverRegistration(
            RegistrationFormObject formObject
    ) throws Exception {
        Gson gson = CommonGsonCreator.createCommonGsonBuilder()
                                     .disableHtmlEscaping()
                                     .setDateFormat(Globals.DTF).create();
        Map<String, Object> params = new HashMap<>();
        params.put("registrationJson", gson.toJson(formObject));

        Pair<Pair<SimpleGsonResponse, String>, Map<String, String>> result = basicPostRequestSend(
                appProperties.getRegistrationURL(), params, SimpleGsonResponse.class);
        return result.getLeft();
    }

    @Override
    public Pair<SimpleGsonResponse, String> addCode(String code)
            throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("mobileLockCode", code);

        Pair<Pair<SimpleGsonResponse, String>, Map<String, String>> result =
                basicGetRequestSend(appProperties.getAddCodeURL(), params, SimpleGsonResponse.class);
        return result.getLeft();
    }

    @Override
    public Pair<SimpleGsonResponse, String> addEmail(String email)
            throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);

        Pair<Pair<SimpleGsonResponse, String>, Map<String, String>> result =
                basicGetRequestSend(appProperties.getAddEmailURL(), params, SimpleGsonResponse.class);
        return result.getLeft();
    }

    @Override
    public Pair<SimpleGsonResponse, String> registerDevice(
            String deviceId, String gcmRegId)
            throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceType", DeviceType.ANDROID.name());
        params.put("deviceId", deviceId);
        params.put("pushServiceRegId", gcmRegId);

        Pair<Pair<SimpleGsonResponse, String>, Map<String, String>> result =
                basicGetRequestSend(appProperties.getRegisterDeviceURL(), params, SimpleGsonResponse.class);
        Pair<SimpleGsonResponse, String> responseStringPair = result.getLeft();
        if (responseStringPair.getLeft() != null) {
            //       GCMRegistrar.setRegisteredOnServer( true);
        }
        return responseStringPair;
    }

    @Override
    public Pair<SimpleGsonResponse, String> unregisterDevice(String deviceId, String gcmRegId)
            throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceId", deviceId);
        params.put("pushServiceRegId", gcmRegId);

        Pair<Pair<SimpleGsonResponse, String>, Map<String, String>> result =
                basicGetRequestSend(appProperties.getUnregisterDeviceURL(), params, SimpleGsonResponse.class);
        return result.getLeft();
    }
}
