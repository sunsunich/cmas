package org.cmas.service;

import android.app.Activity;

public interface RegistrationService {

    String registerUser(Activity activity,
                        String countryName,
                        String firstName,
                        String lastName,
                        String dobStr
    );

    String addCode(Activity activity, String email, String code, String repeatCode);
}
