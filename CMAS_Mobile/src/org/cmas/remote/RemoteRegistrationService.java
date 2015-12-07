package org.cmas.remote;

import android.content.Context;
import android.util.Pair;
import org.cmas.entities.User;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.json.user.RegisterNewUserReply;

public interface RemoteRegistrationService extends BaseRemoteService {

    Pair<RegisterNewUserReply, String> registerUsername(Context context, String username, String password)
            throws Exception;

    Pair<SimpleGsonResponse, String> addCode(
            Context context,
            String code
    ) throws Exception;

    Pair<SimpleGsonResponse, String> addEmail(
            Context context,
            String email
    ) throws Exception;

    Pair<User, String> loginUsername(
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
