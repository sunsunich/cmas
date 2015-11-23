package org.cmas.presentation.validator.admin;

import org.cmas.entities.User;
import org.cmas.presentation.service.user.AllUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class EditUserValidator implements Validator {

    @Autowired
    private AllUsersService allUsersService;

    @Override
    public boolean supports(Class clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        String emailField = "email";
        if (!errors.hasFieldErrors(emailField)) {
            String value = (String) errors.getFieldValue(emailField);
            if (!allUsersService.isEmailUnique(user.getRole(), user.getId(), value)) {
                errors.rejectValue(emailField, "validation.emailExists");
            }
        }
    }
}
