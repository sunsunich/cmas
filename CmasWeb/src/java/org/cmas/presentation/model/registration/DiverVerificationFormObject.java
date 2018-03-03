package org.cmas.presentation.model.registration;

import org.cmas.Globals;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;


public class DiverVerificationFormObject  {

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyCountry")
    private String country;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyName")
    private String name;

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
