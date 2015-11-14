package org.cmas.presentation.service.user;

import org.cmas.presentation.entities.user.UserClient;
import org.cmas.presentation.model.user.EmailEditFormObject;
import org.cmas.presentation.model.user.PasswordEditFormObject;
import org.cmas.presentation.model.user.UserFormObject;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;


public interface UserService
        {

	UserClient createNew();

	@Transactional
    void changePassword(UserClient user, PasswordEditFormObject formObject, BindingResult errors, String ip);

    void checkUserPassword( String codedPassword, String enteredPassword
                          , String propertyName, String validationMessage, BindingResult errors);

    boolean isEmailUnique(@Nullable UserClient user, String email);

    @Transactional
    void editUser(UserFormObject formObject, UserClient user, String ip);

    @Transactional
    void changeEmail(UserClient user, EmailEditFormObject formObject, BindingResult errors);
}
