package org.cmas.presentation.service.user;

import org.cmas.entities.User;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.user.EmailEditFormObject;
import org.cmas.presentation.model.user.PasswordEditFormObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;


public interface UserService<T extends User> {

    T add(Registration registration, String ip);

	@Transactional
    void changePassword(T user, PasswordEditFormObject formObject, BindingResult errors, String ip);

    void checkUserPassword( String codedPassword, String enteredPassword
                          , String propertyName, String validationMessage, BindingResult errors);

    @Transactional
    void editUser(T user, String ip);

    @Transactional
    void changeEmail(T user, EmailEditFormObject formObject, BindingResult errors);
}
