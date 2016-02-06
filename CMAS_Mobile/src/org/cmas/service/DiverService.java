package org.cmas.service;

import android.content.Context;
import org.cmas.entities.diver.Diver;

public interface DiverService {

    Diver getByEmail(Context context, String email) throws Exception;

    Diver getById(Context context, long id) throws Exception;

    void persist(Context context, Diver user, boolean isNewUser);
}
