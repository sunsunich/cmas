package org.cmas.presentation.service.admin;

import org.cmas.presentation.dao.user.UserBalanceDao;
import org.cmas.presentation.dao.user.UserDao;
import org.cmas.presentation.dao.user.UserEventDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.entities.user.UserClient;
import org.cmas.presentation.entities.user.UserEvent;
import org.cmas.presentation.entities.user.UserEventType;
import org.cmas.presentation.model.admin.AdminUserFormObject;
import org.cmas.presentation.model.admin.PasswordChangeFormObject;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.presentation.service.user.RegistrationService;
import org.cmas.presentation.service.user.UserService;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.presentation.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public class AdminServiceImpl implements AdminService {
	
    private final Logger log = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private MailService mailer;

    @Autowired
    private Md5PasswordEncoder passwordEncoder;


    @Autowired
    @Qualifier("registrationService")
    private RegistrationService registrationService;

    @Autowired
	@Qualifier("userService")
	private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserEventDao userEventDao;

    @Autowired
    private UserBalanceDao userBalanceDao;
    
    @Override
    // @Transactional
    public UserClient processConfirmRegistration(RegistrationConfirmFormObject formObject, String ip) {
        Registration registration = registrationService.getRegistrationDao().getById(formObject.getRegId());
        UserClient user = userService.createNew();
        transferToUser(user, registration);

        userDao.saveModel(user);

        //userFinancesDao.save(user.getUserBalance());
        registrationService.getRegistrationDao().deleteModel(registration);

        userEventDao.save( new UserEvent(UserEventType.REGISTER, user
                         , ip, "ordinary")
                         );
        
        try {
            mailer.regCompleteNotify(user);
        } catch (Exception e) {
            log.error("error send email for new user " + user, e);
        }

        return user;
    }


    private void transferToUser(UserClient user, Registration registration) {

        user.setEmail(registration.getEmail());
        // Устанавливаем дату подтверждения регистрации
        user.setDateReg(new Date());
        //String realPassword = passwordEncoder.encodePassword(entity.getPassword(), UserDetails.SALT);
        String realPassword = registration.getPassword();
        user.setPassword(realPassword);
        user.setEnabled(true);
        // Устанавливаем роли по умолчанию
        user.setRole(Role.ROLE_USER);
       
    }


    @Override
    @Transactional
    public void editUser(AdminUserFormObject formObject) {
        Long id = formObject.getId();
        if (id == null) {
            throw new BadRequestException();
        }
        UserClient user = userDao.getById(id);
        if (user == null) {
            throw new BadRequestException();
        }
        formObject.transferToEntity(user);
        userDao.updateModel(user);

    }

    @Override
    @Transactional
    public void changePassword(PasswordChangeFormObject formObject) {
        if (formObject.getUserId() != null) {
            String realPassword = passwordEncoder.encodePassword(formObject.getPasswd(), UserDetails.SALT);
            UserClient user = userDao.getById(formObject.getUserId());
            user.setPassword(realPassword);
            userDao.updateModel(user);
        }
    }

}
