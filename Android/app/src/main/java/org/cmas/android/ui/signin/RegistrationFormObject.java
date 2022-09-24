package org.cmas.android.ui.signin;

import com.google.myjson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RegistrationFormObject implements Serializable {

    @Expose
    public final String email;
    @Expose
    public final String firstName;
    @Expose
    public final String lastName;
    @Expose
    public final String dob;
    @Expose
    public final String countryCode;
    @Expose
    public final String federationId;
    @Expose
    public final String areaOfInterest;
    @Expose
    public final boolean termsAndCondAccepted;
    @Expose
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
