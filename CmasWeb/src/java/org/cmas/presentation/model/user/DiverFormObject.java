package org.cmas.presentation.model.user;

import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.model.Transferable;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;

import java.util.Date;

public class DiverFormObject implements Transferable<Diver>, Validatable {

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyName")
    private String firstName;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyName")
    private String lastName;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String dob;

    @Length(max = Globals.HALF_MAX_LENGTH, message = "validation.maxLength")
    private String areaOfInterest;

    @Length(max = Globals.MAX_COUNTRY_CODE_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String countryCode;

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateDate(errors, dob, "dob", "validation.incorrectField", Globals.getDTF());
    }

    @Override
    public void transferToEntity(Diver entity) {
        throw new UnsupportedOperationException("DiverFormObject cannot transfer itself to entity");
    }

    @Override
    public void transferFromEntity(Diver entity) {
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        Date entityDo = entity.getDob();
        dob = entityDo == null ? "" : Globals.getDTF().format(entityDo);
        areaOfInterest = entity.getAreaOfInterest();
        countryCode = entity.getCountry().getCode();
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAreaOfInterest() {
        return areaOfInterest;
    }

    public void setAreaOfInterest(String areaOfInterest) {
        this.areaOfInterest = areaOfInterest;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
