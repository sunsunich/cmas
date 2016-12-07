package org.cmas.presentation.service.user;

import org.cmas.Globals;
import org.cmas.entities.Country;
import org.cmas.entities.Role;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.user.RegistrationDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.registration.DiverRegistrationFormObject;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.presentation.service.sports.NationalFederationService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.remote.ErrorCodes;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.text.ParseException;
import java.util.List;


public class RegistrationServiceImpl implements RegistrationService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private MailService mailer;

    @Autowired
    private HibernateSpringValidator validator;

    @Autowired
    private RegistrationDao registrationDao;

    @Autowired
    private Md5PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private AllUsersService allUsersService;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private NationalFederationService federationService;

    @Autowired
    private PersonalCardService personalCardService;

    private int freeDiversRegistrationsAmount;

    @Override
    public void validate(DiverRegistrationFormObject formObject, BindingResult errors) {
        validator.validate(formObject, errors);
        if (errors.hasErrors()) {
            return;
        }
        Country country = countryDao.getByCode(formObject.getCountry());
        if (country == null) {
            errors.rejectValue("country", "validation.incorrectField");
            return;
        }
        try {
            Diver diver = federationService.getDiver(
                    formObject.getFirstName(),
                    formObject.getLastName(),
                    Globals.getDTF().parse(formObject.getDob()),
                    country
            );

            if (diver == null) {
                errors.reject("validation.noPersonInFederation");
                return;
            }
            if (diver.isHasPayed()) {
                errors.reject("validation.diverAlreadyRegistered");
            }

        } catch (ParseException ignored) {
            errors.rejectValue("dob", "validation.incorrectField");
        }
    }

    @Override
    public void validateEmail(Registration formObject, Errors errors) {
        // проверяем на уникальность email
        String emailFieldName = "email";
        if (!errors.hasFieldErrors(emailFieldName)) {
            String emailValue = formObject.getEmail();
            if (!allUsersService.isEmailUnique(Role.valueOf(formObject.getRole()), null, emailValue)) {
                errors.rejectValue(emailFieldName, ErrorCodes.EMAIL_ALREADY_EXISTS);
            }
        }
    }

    @Override
    public void validateConfirm(RegistrationConfirmFormObject formObject, BindingResult errors) {
        validator.validate(formObject, errors);
        Long id = formObject.getRegId();
        String sec = formObject.getSec();

        Registration reg = registrationDao.getByIdAndSec(id, sec);
        if (reg == null) {
            errors.rejectValue("sec", "validation.incorrectField");
        }
    }

    @Nullable
    @Override
    @Transactional
    public Diver setupDiver(DiverRegistrationFormObject formObject) {
        Country country = countryDao.getByCode(formObject.getCountry());
        try {
            Diver diver = federationService.getDiver(
                    formObject.getFirstName(),
                    formObject.getLastName(),
                    Globals.getDTF().parse(formObject.getDob()),
                    country
            );
            String generatedPassword = passwordService.generatePassword();
            String newPasswd = passwordEncoder.encodePassword(generatedPassword, UserDetails.SALT);
            diver.setGeneratedPassword(generatedPassword);
            diver.setPassword(newPasswd);
            if (diver.getLocale() == null) {
                diver.setLocale(formObject.getLocale());
            }

            diverDao.updateModel(diver);

            try {
                mailer.sendDiverPassword(diver);
            } catch (Exception e) {
                log.error("error send setup email for diver " + diver.getId(), e);
                return null;
            }

            return diver;
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void createDiverPrimaryCard(Diver diver) {
        if (diver.getPrimaryPersonalCard() == null) {
            personalCardService.generatePrimaryCard(
                    diver, diverDao
            );
        }
    }

    @Override
    public boolean isFreeRegistration(Diver diver) {
        return diverDao.getFullyRegisteredDiverCnt() <= freeDiversRegistrationsAmount;
    }

    /**
     * @return список желающих регистрироваться
     */
    @Override
    @Transactional
    public List<Registration> getReadyToRegister() {
        return registrationDao.getReadyToRegister();
    }

    /**
     * удаляет регистрацию. что такое регистрация - вопрос отдельного обсуждения.
     *
     * @param id - ID регистрации
     */
    @Override
    @Transactional
    public void delete(long id) {
        Registration reg = registrationDao.getModel(id);
        if (reg != null) {
            registrationDao.deleteModel(reg);
        }
    }

    @Required
    public void setFreeDiversRegistrationsAmount(int freeDiversRegistrationsAmount) {
        this.freeDiversRegistrationsAmount = freeDiversRegistrationsAmount;
    }
}