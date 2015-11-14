package org.cmas.presentation.service.user;

import org.cmas.presentation.dao.billing.FinSettingsDao;
import org.cmas.presentation.dao.user.RegistrationDao;
import org.cmas.presentation.dao.user.UserDao;
import org.cmas.presentation.dao.user.UserEventDao;
import org.cmas.presentation.entities.user.UserClient;
import org.cmas.presentation.entities.user.UserEvent;
import org.cmas.presentation.entities.user.UserEventType;
import org.cmas.presentation.model.user.EmailEditFormObject;
import org.cmas.presentation.model.user.PasswordEditFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.model.user.UserFormObject;
import org.cmas.presentation.service.EntityServiceImpl;
import org.cmas.presentation.service.mail.MailService;
import org.hibernate.HibernateException;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;


public class UserServiceImpl extends EntityServiceImpl<UserFormObject,UserClient>
        implements UserService{

	private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MailService mailer;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserEventDao userEventDao;

    @Autowired
    @Qualifier(value = "registrationDao")
    private RegistrationDao registrationDao;

    @Autowired
    private Md5PasswordEncoder passwordEncoder;

    @Autowired
    private FinSettingsDao finSettingsDao;

    @Override
    public UserClient createNew() {
        UserClient user = userDao.createNew(entityClass);
//        UserBalance userBalance = new UserBalance(user);
//        FinSettings finSettings = finSettingsDao.getFinSettings();
//        userBalance.setDiscountPercent(finSettings.getDefaultDiscountPercent());
//        user.setUserBalance(userBalance);
        return user;
    }



    @Override
	@SuppressWarnings({"MethodWithMoreThanThreeNegations"})
	@Transactional
    public void changePassword(UserClient user, PasswordEditFormObject formObject, BindingResult errors, String ip){
        validator.validate(formObject, errors);
        if (!errors.hasErrors()) {
            checkUserPassword(user.getPassword(), formObject.getOldPassword(), "oldPassword", "validation.oldPasswordRejected", errors);

        }
        if (!errors.hasErrors()) {
            String newPasswd = passwordEncoder.encodePassword(formObject.getPassword(), UserDetails.SALT);
            user.setPassword(newPasswd);
            entityDao.updateModel(user);
            userEventDao.save(new UserEvent(UserEventType.PASSWORD_CHANGE, user, ip, ""));
        }
    }

    @Override
    public void checkUserPassword( String codedPassword, String enteredPassword
                                 , String propertyName, String validationMessage, BindingResult errors) {
        String codedEnteredPassword = passwordEncoder.encodePassword(enteredPassword, UserDetails.SALT);
        if (!codedEnteredPassword.equals(codedPassword)) {
            errors.rejectValue(propertyName, validationMessage);
        }
    }

    @Override
	public boolean isEmailUnique(@Nullable UserClient user, String email){
        Long userId =  user == null ? null : user.getNullableId();
        return userDao.isEmailUnique(email, userId) && registrationDao.isEmailUnique(email);
    }

    @Override
	@Transactional
    public void editUser(UserFormObject formObject, UserClient user, String ip) {
        formObject.transferToEntity(user);
        userDao.updateModel(user);
    }

    @Override
	@Transactional
	@SuppressWarnings({"MethodWithMoreThanThreeNegations"})
    public void changeEmail(UserClient user, EmailEditFormObject formObject, BindingResult errors){
        validator.validate(formObject, errors);
		String emailFieldName = "email";
        if (!errors.hasFieldErrors(emailFieldName)) {
            String emailValue = formObject.getEmail();
            if (!isEmailUnique(user, emailValue)) {
                errors.rejectValue(emailFieldName, "validation.emailExists");
            }
        }
        
        String passwordFieldName = "password";
        if (!errors.hasFieldErrors(passwordFieldName)) {
            String nowPassword = user.getPassword();
            String passFromFO = passwordEncoder.encodePassword(formObject.getPassword(), UserDetails.SALT);
            if (!passFromFO.equals(nowPassword)) {
                errors.rejectValue("password", "validation.passwordIncorrect");
            }
        }
		String email = formObject.getEmail();
		// Узнаем, а действительно ли пользователь сменил почту?
		if (!errors.hasErrors() && !user.getEmail().equals(email)) {
			// Сменил, собираем md5 и обновляем user
			try {
				String md5 = passwordEncoder.encodePassword(email + user.getUsername(), UserDetails.SALT);
				user.setNewMail(email);
				user.setMd5newMail(md5);
				mailer.confirmChangeEmail(user);
				entityDao.updateModel(user);
			}
			catch (HibernateException e) {
				log.error("error working with db", e);
				errors.reject("validation.internal");
			}
			catch (Exception e) {
				log.error("error sending mail", e);
				errors.reject("validation.internal.email");
			}
		}
    }
}
