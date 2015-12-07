package org.cmas.service;

import android.app.Activity;

public interface RegistrationService {

    String registerUser(Activity activity, String email, String password, String repeatPassword);

    String addCode(Activity activity, String email, String code, String repeatCode);
}
