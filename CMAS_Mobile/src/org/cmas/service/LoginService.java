package org.cmas.service;

import android.content.Context;
import android.util.Pair;
import org.cmas.entities.User;

public interface LoginService {

    Pair<User, String> loginUser(Context context, String username, String password);

    void logout(Context context, String username);

    void reLoginUserOnServer(Context context) throws ReLoginException;
}
