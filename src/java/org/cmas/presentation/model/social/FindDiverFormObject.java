package org.cmas.presentation.model.social;

import org.cmas.Globals;
import org.cmas.entities.diver.DiverType;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.springframework.validation.Errors;

/**
 * Created on Jun 12, 2016
 *
 * @author Alexander Petukhov
 */
public class FindDiverFormObject implements Validatable {

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String diverType;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String country;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    @NotEmpty(message = "validation.emptyField")
    private String name;

    @Override
    public void validate(Errors errors) {
        ValidatorUtils.validateEnum(errors, diverType, DiverType.class, "diverType", "validation.incorrectField");
    }

    public String getDiverType() {
        return diverType;
    }

    public void setDiverType(String diverType) {
        this.diverType = diverType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


