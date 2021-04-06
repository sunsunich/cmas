package org.cmas.android.ui.signin;

import java.util.ArrayList;
import java.util.List;

public class RegistrationFormObject {

    public final String email;
    public final String firstName;
    public final String lastName;
    public final String dob;
    public final String countryCode;
    public final String federationId;
    public final String areaOfInterest;

    public final boolean termsAndCondAccepted;

    public final List<String> images = new ArrayList<>();

    public RegistrationFormObject(String email,
                                  String firstName,
                                  String lastName,
                                  String dob,
                                  String countryCode,
                                  String federationId,
                                  String areaOfInterest,
                                  boolean termsAndCondAccepted) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.countryCode = countryCode;
        this.federationId = federationId;
        this.areaOfInterest = areaOfInterest;
        this.termsAndCondAccepted = termsAndCondAccepted;
    }
}
