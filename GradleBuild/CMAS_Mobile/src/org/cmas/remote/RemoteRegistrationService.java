package org.cmas.remote;

import android.content.Context;
import android.util.Pair;
import org.cmas.entities.diver.Diver;
import org.cmas.json.JsonBindingResultModel;
import org.cmas.json.SimpleGsonResponse;

public interface RemoteRegistrationService extends BaseRemoteService {

    Pair<JsonBindingResultModel, String> checkDiverRegistration(
            Context context,
            String countryCode, String firstName, String lastName, String dobStr
    ) throws Exception;

    Pair<SimpleGsonResponse, String> addCode(
            Context context,
            String code
    ) throws Exception;

    Pair<SimpleGsonResponse, String> addEmail(
            Context context,
            String email
    ) throws Exception;

    Pair<Diver, String> login(
            Context context,
            LoginData loginData
    ) throws Exception;

    Pair<SimpleGsonResponse, String> registerDevice(
            Context context,
            String deviceId,
            String gcmRegId
    ) throws Exception;

    Pair<SimpleGsonResponse, String> unregisterDevice(
            Context context,
            String deviceId,
            String gcmRegId
    ) throws Exception;
}
