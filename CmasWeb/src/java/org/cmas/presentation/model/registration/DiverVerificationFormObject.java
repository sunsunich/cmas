package org.cmas.presentation.model.registration;

import org.cmas.Globals;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.springframework.validation.Errors;


public class DiverVerificationFormObject implements Validatable {

    private String country;
    private String name;

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateEmpty(errors, country, "country", "validation.emptyCountry");
        ValidatorUtils.validateLength(errors, country, "country", "validation.maxLength", Globals.MAX_LENGTH);
        ValidatorUtils.validateEmpty(errors, name, "name", "validation.emptyName");
        ValidatorUtils.validateLength(errors, name, "name", "validation.maxLength", Globals.MAX_LENGTH);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
