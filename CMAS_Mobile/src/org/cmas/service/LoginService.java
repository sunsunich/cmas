package org.cmas.service;

import android.content.Context;
import android.util.Pair;
import org.cmas.entities.diver.Diver;

public interface LoginService {

    Pair<Diver, String> loginUser(Context context, String email, String password);

    void logout(Context context, String username);

    void reLoginUserOnServer(Context context) throws ReLoginException;
}
