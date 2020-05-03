package org.cmas.presentation.validator.user;

import org.cmas.entities.Country;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.model.user.DiverFormObject;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created on Jan 09, 2017
 *
 * @author Alexander Petukhov
 */
public class DiverEditValidator implements Validator {

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private HibernateSpringValidator validator;

    @Override
    public boolean supports(Class aClass) {
        return DiverFormObject.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        validator.validate(o, errors);
        if (errors.hasErrors()) {
            return;
        }
        DiverFormObject diverFormObject = (DiverFormObject) o;
        Country country = countryDao.getByCode(diverFormObject.getCountryCode());
        if (country == null) {
            errors.rejectValue("countryCode", "validation.incorrectField");
        }
    }
}
