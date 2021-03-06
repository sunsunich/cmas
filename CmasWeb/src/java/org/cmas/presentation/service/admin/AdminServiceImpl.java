package org.cmas.presentation.service.admin;

import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.UserBalance;
import org.cmas.entities.amateur.Amateur;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.diver.NotificationsCounter;
import org.cmas.entities.logbook.LogbookVisibility;
import org.cmas.entities.sport.Athlete;
import org.cmas.presentation.dao.cards.PersonalCardDao;
import org.cmas.presentation.dao.user.UserBalanceDao;
import org.cmas.presentation.dao.user.sport.AthleteDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.dao.user.sport.NotificationsCounterDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.model.admin.AdminUserFormObject;
import org.cmas.presentation.model.admin.PasswordChangeFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.presentation.service.user.DiverService;
import org.cmas.presentation.service.user.UserService;
import org.cmas.util.http.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

public class AdminServiceImpl implements AdminService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MailService mailer;

    @Autowired
    @Qualifier("amateurService")
    private UserService<Amateur> amateurService;

    @Autowired
    @Qualifier("diverService")
    private DiverService diverService;

    @Autowired
    private AthleteDao athleteDao;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private NotificationsCounterDao notificationsCounterDao;

    @Autowired
    private UserBalanceDao userBalanceDao;

    @Autowired
    private PersonalCardDao personalCardDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    // @Transactional
    public BackendUser processConfirmRegistration(Registration registration, String ip) {
        UserService userService = null;
        Role role = Role.valueOf(registration.getRole());
        switch (role) {
            case ROLE_DIVER:
                userService = diverService;
                break;
            case ROLE_AMATEUR:
                userService = amateurService;
                break;
            case ROLE_ATHLETE:
                break;
            case ROLE_ADMIN:
                break;
        }
        if (userService == null) {
            throw new IllegalStateException();
        }
        User user = userService.add(registration, ip);
        mailer.regCompleteNotify(user);
        return new BackendUser(user);
    }


    @Override
    public void cloneUser(Diver diver) {
        Diver newDiver = diverDao.createNew(Diver.class);
        newDiver.setBot(true);
        newDiver.setLocale(diver.getLocale());
        newDiver.setFederation(diver.getFederation());
        newDiver.setCountry(diver.getCountry());
        newDiver.setRole(Role.ROLE_DIVER);
        newDiver.setDateReg(diver.getDateReg());
        newDiver.setDefaultVisibility(LogbookVisibility.FRIENDS);
        newDiver.setDiverRegistrationStatus(DiverRegistrationStatus.NEVER_REGISTERED);
        newDiver.setPreviousRegistrationStatus(DiverRegistrationStatus.NEVER_REGISTERED);

        UserBalance userBalance = new UserBalance();
        userBalance.setBalance(diver.getUserBalance().getBalance());
        userBalanceDao.save(userBalance);
        newDiver.setUserBalance(userBalance);


        newDiver.setLastName(diver.getLastName());
        newDiver.setDob(diver.getDob());
        newDiver.setDiverType(diver.getDiverType());
        newDiver.setDiverLevel(diver.getDiverLevel());
        newDiver.setInstructor(diver.getInstructor());
        newDiver.setEmail(diver.getEmail() + "@mailinator.com");

        Long id = (Long) diverDao.save(newDiver);
        newDiver.setEmail(id + "@mailinator.com");
        newDiver.setFirstName(diver.getFirstName() + "Bot");

        for (PersonalCard card : diver.getCards()) {
            PersonalCard newCard = new PersonalCard();

            newCard.setNumber(id + card.getNumber());

            newCard.setFederationName(card.getFederationName());
            newCard.setCardType(card.getCardType());
            newCard.setDiverType(card.getDiverType());
            newCard.setDiverLevel(card.getDiverLevel());
            newCard.setDiver(newDiver);
            personalCardDao.save(newCard);
        }

        diverDao.updateModel(newDiver);
        NotificationsCounter notificationsCounter = new NotificationsCounter();
        notificationsCounter.setDiver(newDiver);
        notificationsCounterDao.save(notificationsCounter);
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
