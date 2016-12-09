package org.cmas.presentation.model.social;

import org.cmas.Globals;
import org.cmas.entities.diver.DiverType;
import org.cmas.presentation.validator.Validatable;
import org.cmas.presentation.validator.ValidatorUtils;
import org.cmas.util.StringUtil;
import org.hibernate.validator.Length;
import org.springframework.validation.Errors;

/**
 * Created on Jun 12, 2016
 *
 * @author Alexander Petukhov
 */
public class FindDiverFormObject implements Validatable {

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String diverType;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String country;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String name;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String dob;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String cmasCardNumber;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String federationCardNumber;

    @Length(max = Globals.MAX_LENGTH, message = "validation.maxLength")
    private String federationCountry;

    @Override
    public void validate(Errors errors) {
        if (StringUtil.isTrimmedEmpty(cmasCardNumber)) {
            if (StringUtil.isTrimmedEmpty(federationCardNumber)
                && StringUtil.isTrimmedEmpty(federationCountry)) {
                if (StringUtil.isTrimmedEmpty(diverType)) {
                    errors.rejectValue("diverType", "validation.emptyField");
                } else {
                    ValidatorUtils.validateEnum(errors,
                                                diverType,
                                                DiverType.class,
                                                "diverType",
                                                "validation.incorrectField");
                }
                if (StringUtil.isTrimmedEmpty(name) || name.length() < 3) {
                    errors.rejectValue("name", "validation.incorrectField");
                }
                if (StringUtil.isTrimmedEmpty(country)) {
                    errors.rejectValue("country", "validation.emptyField");
                }
            } else {
                if (StringUtil.isTrimmedEmpty(federationCardNumber)) {
                    errors.rejectValue("federationCardNumber", "validation.emptyField");
                }
                if (StringUtil.isTrimmedEmpty(federationCountry)) {
                    errors.rejectValue("federationCountry", "validation.emptyField");
                }
            }
        }
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCmasCardNumber() {
        return cmasCardNumber;
    }

    public void setCmasCardNumber(String cmasCardNumber) {
        this.cmasCardNumber = cmasCardNumber;
    }

    public String getFederationCardNumber() {
        return federationCardNumber;
    }

    public void setFederationCardNumber(String federationCardNumber) {
        this.federationCardNumber = federationCardNumber;
    }

    public String getFederationCountry() {
        return federationCountry;
    }

    public void setFederationCountry(String federationCountry) {
        this.federationCountry = federationCountry;
    }
}


