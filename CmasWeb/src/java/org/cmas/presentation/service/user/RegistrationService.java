package org.cmas.presentation.service.user;

import org.cmas.entities.diver.Diver;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.registration.DiverRegistrationDTO;
import org.cmas.presentation.model.registration.DiverRegistrationFormObject;
import org.cmas.presentation.model.registration.FullDiverRegistrationFormObject;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Locale;

public interface RegistrationService {

    void validate(DiverRegistrationFormObject formObject, Errors errors);

    void validateFromMobile(FullDiverRegistrationFormObject formObject, Errors errors);

    void validateEmail(FullDiverRegistrationFormObject formObject, Errors errors);

    void validateConfirm(RegistrationConfirmFormObject formObject, Errors errors);

    void setupCMASDiver(Diver diver, Locale locale);

    void generateAllCardsImages(Diver diver);

    @Transactional
    Registration add(FullDiverRegistrationFormObject formObject);

    @Transactional
    List<Registration> getReadyToRegister();

    @Nullable
    @Transactional
    List<DiverRegistrationDTO> getDiversForRegistration(DiverRegistrationFormObject formObject);

    void delete(long id);

}
