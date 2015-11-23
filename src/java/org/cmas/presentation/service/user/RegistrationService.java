package org.cmas.presentation.service.user;

import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.List;

public interface RegistrationService {

    void validate(Registration formObject, BindingResult errors);

    void validateEmail(Registration formObject, Errors errors);

    void validateConfirm(RegistrationConfirmFormObject formObject, BindingResult errors);

    /**
     * @return список желающих регистрироваться
     */
    @Transactional
    List<Registration> getReadyToRegister();

    @Nullable
    Registration add(Registration formObject, BindingResult result);

    void delete(long id);

}
