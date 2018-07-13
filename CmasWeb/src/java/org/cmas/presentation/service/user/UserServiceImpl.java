package org.cmas.presentation.service.user;

import org.cmas.entities.Country;
import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.UserBalance;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.billing.FinSettingsDao;
import org.cmas.presentation.dao.user.RegistrationDao;
import org.cmas.presentation.dao.user.UserBalanceDao;
import org.cmas.presentation.dao.user.UserDao;
import org.cmas.presentation.dao.user.UserEventDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.entities.user.UserEvent;
import org.cmas.presentation.entities.user.UserEventType;
import org.cmas.presentation.model.user.EmailEditFormObject;
import org.cmas.presentation.model.user.PasswordEditFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.EntityServiceImpl;
import org.cmas.presentation.service.mail.MailService;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Date;


public class UserServiceImpl<T extends User> extends EntityServiceImpl<T>
        implements UserService<T> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MailService mailer;

    @Autowired
    protected UserEventDao userEventDao;

    @Autowired
    private Md5PasswordEncoder passwordEncoder;

    @Autowired
    private FinSettingsDao finSettingsDao;

    @Autowired
    protected UserBalanceDao userBalanceDao;

    @Autowired
    private AllUsersService allUsersService;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private RegistrationDao registrationDao;

    @Override
    @Transactional
    public T add(Registration registration, String ip) {
        T user = ((UserDao<T>) entityDao).createNew(entityClass);
//        UserBalance userBalance = new UserBalance(user);
//        FinSettings finSettings = finSettingsDao.getFinSettings();
//        userBalance.setDiscountPercent(finSettings.getDefaultDiscountPercent());
//        user.setUserBalance(userBalance);

        user.setDateReg(new Date());
        //String realPassword = passwordEncoder.encodePassword(entity.getPassword(), UserDetails.SALT);
        String realPassword = registration.getPassword();
        user.setPassword(realPassword);
        user.setLastAction(new Date());
        user.setRole(Role.valueOf(registration.getRole()));
        user.setLocale(registration.getLocale());

        user.setEmail(registration.getEmail());
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        Country country = countryDao.getByCode(registration.getCountry());
        user.setCountry(country);
        user.setDob(registration.getDob());

        UserBalance userBalance = new UserBalance();
        userBalanceDao.save(userBalance);
        user.setUserBalance(userBalance);

        Long id = (Long) entityDao.save(user);
        user.setId(id);

        userEventDao.save(new UserEvent(UserEventType.REGISTER, ip, "ordinary", user));
        registrationDao.deleteModel(registration);

        return user;
    }

    @Override
    @SuppressWarnings({"MethodWithMoreThanThreeNegations"})
    @Transactional
    public void changePassword(T user, PasswordEditFormObject formObject, BindingResult errors, String ip) {
        validator.validate(formObject, errors);
        if (!errors.hasErrors()) {
            checkUserPassword(user.getPassword(), formObject.getOldPassword(), "oldPassword", "validation.oldPasswordRejected", errors);
        }
        if (!errors.hasErrors()) {
            String newPasswd = passwordEncoder.encodePassword(formObject.getPassword(), UserDetails.SALT);
            user.setPassword(newPasswd);
            entityDao.updateModel(user);
            //         userEventDao.save(new UserEvent(UserEventType.PASSWORD_CHANGE, user, ip, ""));
        }
    }

    @Override
    public void checkUserPassword(String codedPassword, String enteredPassword
            , String propertyName, String validationMessage, BindingResult errors) {
        String codedEnteredPassword = passwordEncoder.encodePassword(enteredPassword, UserDetails.SALT);
        if (!codedEnteredPassword.equals(codedPassword)) {
            errors.rejectValue(propertyName, validationMessage);
        }
    }

    @Override
    @Transactional
    public void editUser(T user, String ip) {
        //todo make it work formObject.transferToEntity(user);
        entityDao.updateModel(user);
    }

    @Override
    @Transactional
    @SuppressWarnings({"MethodWithMoreThanThreeNegations"})
    public void changeEmail(T user, EmailEditFormObject formObject, BindingResult errors) {
        validator.validate(formObject, errors);
        String emailFieldName = "email";
        if (!errors.hasFieldErrors(emailFieldName)) {
            String emailValue = formObject.getEmail();
            if (!allUsersService.isEmailUnique(user.getRole(), user.getId(), emailValue)) {
                errors.rejectValue(emailFieldName, "validation.emailExists");
            }
        }

        String passwordFieldName = "password";
        if (!errors.hasFieldErrors(passwordFieldName)) {
            String nowPassword = user.getPassword();
            String passFromFO = passwordEncoder.encodePassword(formObject.getPassword(), UserDetails.SALT);
            if (!passFromFO.equals(nowPassword)) {
                errors.rejectValue("password", "validation.passwordRejected");
            }
        }
        String email = formObject.getEmail();
        // Узнаем, а действительно ли пользователь сменил почту?
        if (!errors.hasErrors() && !user.getEmail().equals(email)) {
            // Сменил, собираем md5 и обновляем user
            try {
                String md5 = passwordEncoder.encodePassword(email + user.getEmail(), UserDetails.SALT);
                user.setNewMail(email);
                user.setMd5newMail(md5);
                mailer.confirmChangeEmail(user);
                entityDao.updateModel(user);
            } catch (HibernateException e) {
                log.error("error working with db", e);
                errors.reject("validation.internal");
            } catch (Exception e) {
                log.error("error sending mail", e);
                errors.reject("validation.internal.email");
            }
        }
    }
}
