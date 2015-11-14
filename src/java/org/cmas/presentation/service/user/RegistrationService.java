package org.cmas.presentation.service.user;

import org.cmas.presentation.dao.user.RegistrationDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.registration.RegistrationAddFormObject;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.service.EntityAddService;
import org.cmas.presentation.service.EntityDeleteService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.List;

public interface RegistrationService extends EntityDeleteService, EntityAddService<RegistrationAddFormObject, Registration> {

    void validate(RegistrationAddFormObject formObject, BindingResult errors);

    void validateEmail(RegistrationAddFormObject formObject, Errors errors);

    void validateConfirm(RegistrationConfirmFormObject formObject, BindingResult errors);

    Registration createNew();

    /**
     * @return список желающих регистрироваться
     */
    @Transactional
    List<Registration> getReadyToRegister();

    RegistrationDao getRegistrationDao();
}
