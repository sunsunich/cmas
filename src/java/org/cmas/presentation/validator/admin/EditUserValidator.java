package org.cmas.presentation.validator.admin;

import org.cmas.presentation.dao.user.UserDao;
import org.cmas.presentation.model.admin.AdminUserFormObject;
import org.cmas.presentation.model.user.UserFormObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class EditUserValidator implements Validator {

    @Autowired
    private UserDao userDao;
    
    @Override
    public boolean supports(Class clazz) {
        return UserFormObject.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AdminUserFormObject data = (AdminUserFormObject)target;
        String emailField = "email";
        Long userId = data.getId();

        if (!errors.hasFieldErrors(emailField)) {
            String value = (String) errors.getFieldValue(emailField);
            if (!userDao.isEmailUnique(value, userId)) {
                errors.rejectValue(emailField, "validation.emailExists");
            }
        }
    }
}
