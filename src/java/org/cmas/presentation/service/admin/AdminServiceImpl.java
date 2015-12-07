package org.cmas.presentation.service.admin;

import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.amateur.Amateur;
import org.cmas.entities.sport.Athlete;
import org.cmas.presentation.dao.user.RegistrationDao;
import org.cmas.presentation.dao.user.sport.AthleteDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.admin.AdminUserFormObject;
import org.cmas.presentation.model.admin.PasswordChangeFormObject;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.presentation.service.user.UserService;
import org.cmas.util.http.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

public class AdminServiceImpl implements AdminService {
	
    private final Logger log = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private MailService mailer;

    @Autowired
    private Md5PasswordEncoder passwordEncoder;

    @Autowired
	@Qualifier("athleteService")
	private UserService<Athlete> athleteService;

    @Autowired
    @Qualifier("amateurService")
    private UserService<Amateur> amateurService;

    @Autowired
    private AthleteDao athleteDao;

    @Autowired
    private RegistrationDao registrationDao;
    
    @Override
    // @Transactional
    public BackendUser processConfirmRegistration(RegistrationConfirmFormObject formObject, String ip) {
        Registration registration = registrationDao.getById(formObject.getRegId());
        UserService userService = null;
        Role role = Role.valueOf(registration.getRole());
        switch (role) {
            case ROLE_AMATEUR:
                userService = amateurService;
                break;
            case ROLE_ATHLETE:
                userService = athleteService;
                break;
            case ROLE_ADMIN:
                break;
        }
        if (userService == null) {
            throw new IllegalStateException();
        }
        User user = userService.add(registration, ip);
        try {
            mailer.regCompleteNotify(user);
        } catch (Exception e) {
            log.error("error send email for new user " + user, e);
        }

        return new BackendUser(user);
    }


    @Override
    @Transactional
    public void editUser(AdminUserFormObject formObject) {
        Long id = formObject.getId();
        if (id == null) {
            throw new BadRequestException();
        }
        Athlete user = athleteDao.getById(id);
        if (user == null) {
            throw new BadRequestException();
        }
        formObject.transferToEntity(user);
        athleteDao.updateModel(user);

    }

    @Override
    @Transactional
    public void changePassword(PasswordChangeFormObject formObject) {
        if (formObject.getUserId() != null) {
            String realPassword = passwordEncoder.encodePassword(formObject.getPasswd(), UserDetails.SALT);
            Athlete user = athleteDao.getById(formObject.getUserId());
            user.setPassword(realPassword);
            athleteDao.updateModel(user);
        }
    }

}
