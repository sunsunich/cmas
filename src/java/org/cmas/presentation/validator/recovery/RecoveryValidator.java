package org.cmas.presentation.validator.recovery;

import org.cmas.presentation.dao.user.sport.SportsmanDao;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public abstract class RecoveryValidator implements Validator {

    @Autowired
    protected SportsmanDao sportsmanDao;

    @Autowired
    private HibernateSpringValidator superValidator;

    @Override
	public void validate(Object target, Errors result) {
        superValidator.validate(target,result);
    }
}
