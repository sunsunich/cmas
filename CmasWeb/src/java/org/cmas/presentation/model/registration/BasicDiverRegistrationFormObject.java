package org.cmas.presentation.model.registration;

import com.google.myjson.annotations.Expose;
import org.cmas.Globals;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.cmas.util.StringUtil;
import org.springframework.validation.Errors;

import java.util.Locale;


public class BasicDiverRegistrationFormObject implements Validatable {

    // todo remove country
    @Expose
    private String country;
    @Expose
    private String countryCode;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String dob;

    @Expose
    private String termsAndCondAccepted;

    private Locale locale;

    @Override
    public void validate(Errors errors) {
        if (StringUtil.isTrimmedEmpty(countryCode)) {
            ValidatorUtils.validateEmpty(errors, country, "country", "validation.emptyField");
            ValidatorUtils.validateLength(errors, country, "country", "validation.maxLength", Globals.MAX_LENGTH);
            if (errors.hasFieldErrors("country")) {
                errors.rejectValue("countryCode", "validation.emptyField");
            }
        } else {
            ValidatorUtils.validateEmpty(errors, countryCode, "countryCode", "validation.emptyField");
            ValidatorUtils.validateLength(errors,
                                          countryCode,
                                          "countryCode",
                                          "validation.maxLength",
                                          Globals.MAX_LENGTH);
        }
        ValidatorUtils.validateEmpty(errors, firstName, "firstName", "validation.emptyField");
        ValidatorUtils.validateLength(errors, firstName, "firstName", "validation.maxLength", Globals.MAX_LENGTH);
        ValidatorUtils.validateEmpty(errors, lastName, "lastName", "validation.emptyField");
        ValidatorUtils.validateLength(errors, lastName, "lastName", "validation.maxLength", Globals.MAX_LENGTH);
        ValidatorUtils.validateEmpty(errors, dob, "dob", "validation.emptyField");
        ValidatorUtils.validateDate(errors, dob, "dob", "validation.incorrectField", Globals.getDTF());
        validateTermsAndCond(errors);
    }

    protected void validateTermsAndCond(Errors errors) {
        ValidatorUtils.validateBoolean(errors,
                                       termsAndCondAccepted,
                                       "termsAndCondAccepted",
                                       "validation.termsAndCondNotAccepted");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getTermsAndCondAccepted() {
        return termsAndCondAccepted;
    }

    public void setTermsAndCondAccepted(String termsAndCondAccepted) {
        this.termsAndCondAccepted = termsAndCondAccepted;
    }
}
