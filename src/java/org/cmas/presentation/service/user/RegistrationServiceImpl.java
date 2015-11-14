package org.cmas.presentation.service.user;

import org.cmas.presentation.dao.user.RegistrationDao;
import org.cmas.presentation.dao.user.UserDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.registration.RegistrationAddFormObject;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.EntityServiceImpl;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.dao.HibernateDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.Date;
import java.util.List;


public class RegistrationServiceImpl extends EntityServiceImpl<RegistrationAddFormObject, Registration>
        implements RegistrationService{

    private static final long ONE_MONTH_IN_MILLISECONDS = 30L * 24L * 60L * 60L * 1000L;

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private MailService mailer;

    @Autowired
    private UserDao userDao;

    private RegistrationDao registrationDao;

    @Autowired
    private Md5PasswordEncoder passwordEncoder;

    @Override
    public void validate(RegistrationAddFormObject formObject, BindingResult errors) {
        super.validate(formObject, errors);
        validateEmail(formObject, errors);
    }

    @Override
	public void validateEmail(RegistrationAddFormObject formObject, Errors errors) {
        // проверяем на уникальность email
        String emailFieldName = "email";
        if (!errors.hasFieldErrors(emailFieldName)) {
            String emailValue = formObject.getEmail();
            if (!userDao.isEmailUnique(emailValue) || !registrationDao.isEmailUnique(emailValue)) {
                errors.rejectValue(emailFieldName, "validation.emailExists");
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
        } else {
            // если сейчас дата уже позже чем надо было - опоздали.
            if (reg.getDateConfirm().before(new Date())) {
                errors.rejectValue("sec", "validation.incorrectField");
            }
        }
    }

    @Override
    public Registration createNew() {
        return registrationDao.createNew();
    }

    /**
     * регистрирует клиента из формы регистрации с морды.
     * @param formObject - форма регистрации
     * @return регистрацию
     */
    @Override
	@Transactional
    public Registration add(RegistrationAddFormObject formObject, BindingResult result) {
        Registration entity = formObjectToEntity(formObject);

        String realPassword = passwordEncoder.encodePassword(formObject.getPassword(), UserDetails.SALT);
        entity.setPassword(realPassword);
        // месяц живет
        entity.setDateConfirm(new Date(System.currentTimeMillis() + ONE_MONTH_IN_MILLISECONDS));
        // это секретный код.
        entity.setMd5(passwordEncoder.encodePassword(formObject.getPassword() + formObject.getEmail(), UserDetails.SALT));
        registrationDao.saveModel(entity);
        try {
            mailer.confirmRegistrator(entity);
        } catch (Exception e) {
            log.error("error send confirm email for new registraion " + entity.getNullableId(), e);
        }

        return entity;
    }

    /**
     *
     * @return список желающих регистрироваться
     */
    @Override
	@Transactional
    public List<Registration> getReadyToRegister() {
        return registrationDao.getReadyToRegister();
    }

    /**
     * удаляет регистрацию. что такое регистрация - вопрос отдельного обсуждения.
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

    @Override
	public RegistrationDao getRegistrationDao() {
        return registrationDao;
    }

    @Required
    @Override
    public void setEntityDao(HibernateDao<Registration> entityDao) {
        super.setEntityDao(entityDao);
        registrationDao = (RegistrationDao) entityDao;
    }
}