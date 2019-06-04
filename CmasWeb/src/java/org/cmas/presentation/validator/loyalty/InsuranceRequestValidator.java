package org.cmas.presentation.validator.loyalty;

import org.cmas.entities.Address;
import org.cmas.entities.Country;
import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created on May 31, 2019
 *
 * @author Alexander Petukhov
 */
public class InsuranceRequestValidator implements Validator {

    @Autowired
    private CountryDao countryDao;

    @Override
    public boolean supports(Class aClass) {
        return InsuranceRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        InsuranceRequest insuranceRequest = (InsuranceRequest) o;
        Address address = insuranceRequest.getAddress();
        if (address == null) {
            errors.rejectValue("address", "validation.emptyField");
            return;
        }
        Country country = address.getCountry();
        if (country == null || StringUtil.isTrimmedEmpty(country.getCode())) {
            errors.rejectValue("address.country", "validation.emptyCountry");
        } else {
            Country dbCountry = countryDao.getByCode(country.getCode());
            if (dbCountry == null) {
                errors.rejectValue("address.country", "validation.incorrectField");
            }
        }
        if (StringUtil.isTrimmedEmpty(address.getCity())) {
            errors.rejectValue("address.city", "validation.emptyField");
        }
        if (StringUtil.isTrimmedEmpty(address.getZipCode())) {
            errors.rejectValue("address.zipCode", "validation.emptyField");
        }
        if (StringUtil.isTrimmedEmpty(address.getStreet())) {
            errors.rejectValue("address.street", "validation.emptyField");
        }
        if (StringUtil.isTrimmedEmpty(address.getHouse())) {
            errors.rejectValue("address.house", "validation.emptyField");
        }

        if (insuranceRequest.getGender() == null) {
            errors.rejectValue("gender", "validation.emptyField");
        }
    }
}
