package org.cmas.presentation.model.registration;

import org.cmas.Globals;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;

/**
 * Created on Jun 18, 2018
 *
 * @author Alexander Petukhov
 */
public class DiverRegistrationChooseFormObject implements Validatable {

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String diverId;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String email;

    private String areaOfInterest;

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateLong(
                errors, diverId, "diverId", "validation.incorrectField"
        );
    }

    public String getDiverId() {
        return diverId;
    }

    public void setDiverId(String diverId) {
        this.diverId = diverId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAreaOfInterest() {
        return areaOfInterest;
    }

    public void setAreaOfInterest(String areaOfInterest) {
        this.areaOfInterest = areaOfInterest;
    }
}
