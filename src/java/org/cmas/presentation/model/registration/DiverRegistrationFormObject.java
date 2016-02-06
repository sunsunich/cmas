package org.cmas.presentation.model.registration;

import org.cmas.Globals;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;


public class DiverRegistrationFormObject implements Validatable {

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String country;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String firstName;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String lastName;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String dob;

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateDate(errors, dob, "dob", "validation.incorrectField", Globals.getDTF());
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
