package org.cmas.presentation.model.registration;

import org.cmas.Globals;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

/**
 * Created on Jun 18, 2018
 *
 * @author Alexander Petukhov
 */
public class FullDiverRegistrationFormObject extends BasicDiverRegistrationFormObject {

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String email;

    private String areaOfInterest;

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
