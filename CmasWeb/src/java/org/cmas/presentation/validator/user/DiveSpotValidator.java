package org.cmas.presentation.validator.user;

import org.cmas.entities.Country;
import org.cmas.entities.Toponym;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created on Jan 09, 2017
 *
 * @author Alexander Petukhov
 */
public class DiveSpotValidator implements Validator {

    @Autowired
    private CountryDao countryDao;

    @Override
    public boolean supports(Class aClass) {
        return DiveSpot.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DiveSpot diveSpot = (DiveSpot) o;
        if (StringUtil.isTrimmedEmpty(diveSpot.getName())) {
            errors.rejectValue("diveDate", "validation.emptyField");
        }
        if (diveSpot.getLatitude() <= 0.0) {
            errors.rejectValue("latitude", "validation.incorrectNumber");
        }
        if (diveSpot.getLongitude() <= 0.0) {
            errors.rejectValue("longitude", "validation.incorrectNumber");
        }
        Country country = diveSpot.getCountry();
        if (country != null) {
            String code = country.getCode();
            if (StringUtil.isTrimmedEmpty(code)) {
                errors.rejectValue("code", "validation.emptyField");
            } else {
                Country dbCountry = countryDao.getByCode(code);
                if (dbCountry == null) {
                    errors.rejectValue("code", "validation.incorrectField");
                }
            }
        }
        Toponym toponym = diveSpot.getToponym();
        if (toponym != null) {
            if (StringUtil.isTrimmedEmpty(toponym.getName())) {
                errors.rejectValue("toponym", "validation.emptyField");
            }
        }
    }
}
