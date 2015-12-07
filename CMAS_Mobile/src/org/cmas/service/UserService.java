package org.cmas.service;

import android.content.Context;
import org.cmas.entities.User;

public interface UserService<T extends User> {

    T getByEmail(Context context, String email) throws Exception;

    T getById(Context context, long id) throws Exception;

    void persistUser(Context context, T user, boolean isNewUser);
}
