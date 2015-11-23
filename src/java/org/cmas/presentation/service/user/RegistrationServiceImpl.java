package org.cmas.presentation.service.user;

import org.cmas.entities.Role;
import org.cmas.presentation.dao.user.RegistrationDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.remote.ErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.Date;
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
    private AllUsersService allUsersService;

    public void validate(Registration formObject, BindingResult errors) {
        validator.validate(formObject, errors);
        validateEmail(formObject, errors);
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

    /**
     * регистрирует клиента из формы регистрации с морды.
     *
     * @param formObject - форма регистрации
     * @return регистрацию
     */
    @Override
    @Transactional
    public Registration add(Registration formObject, BindingResult result) {
        formObject.setDateReg(new Date());
        String realPassword = passwordEncoder.encodePassword(formObject.getPassword(), UserDetails.SALT);
        formObject.setPassword(realPassword);
        // это секретный код.
        formObject.setMd5(passwordEncoder.encodePassword(formObject.getPassword() + formObject.getEmail(), UserDetails.SALT));
        registrationDao.saveModel(formObject);
        try {
            mailer.confirmRegistrator(formObject);
        } catch (Exception e) {
            log.error("error send confirm email for new registraion " + formObject.getNullableId(), e);
        }

        return formObject;
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
}