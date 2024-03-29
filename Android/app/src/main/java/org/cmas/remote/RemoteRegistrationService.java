package org.cmas.remote;

import org.apache.commons.lang3.tuple.Pair;
import org.cmas.android.ui.signin.RegistrationFormObject;
import org.cmas.json.JsonBindingResultModel;
import org.cmas.json.SimpleGsonResponse;

public interface RemoteRegistrationService extends BaseRemoteService {

    Pair<SimpleGsonResponse, String> diverRegistration(
            RegistrationFormObject formObject
    ) throws Exception;

    Pair<SimpleGsonResponse, String> addCode(
            String code
    ) throws Exception;

    Pair<SimpleGsonResponse, String> addEmail(
            String email
    ) throws Exception;

//    Pair<Diver, String> login(
//            LoginData loginData
//    ) throws Exception;

    Pair<SimpleGsonResponse, String> registerDevice(
            String deviceId,
            String gcmRegId
    ) throws Exception;

    Pair<SimpleGsonResponse, String> unregisterDevice(
            String deviceId,
            String gcmRegId
    ) throws Exception;
}
