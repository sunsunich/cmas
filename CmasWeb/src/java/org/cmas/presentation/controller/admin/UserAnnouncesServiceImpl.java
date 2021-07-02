package org.cmas.presentation.controller.admin;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.NotificationsCounter;
import org.cmas.presentation.dao.user.sport.NotificationsCounterDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.Base64Coder;
import org.cmas.util.random.Randomazer;
import org.cmas.util.schedule.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserAnnouncesServiceImpl implements UserAnnouncesService {

    static final long EMAIL_SEND_INTERVAL = 1000L * 60L * 2L; // every 2 mins

    private static final int UNSUBSCRIBE_TOKEN_RAND_PART_LENGTH = 7;
    private static final int MAX_MOBILE_EMAILS = 1;
    private final Object mobileReadyMonitor = new Object();

    private static final int MAX_FEDERATION_MANUALS_EMAILS = 1;
    private final Object federationManualsMonitor = new Object();

    private final Object registrationsMonitor = new Object();

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private MailService mailService;

    @Autowired
    private Randomazer randomazer;

    @Autowired
    private NotificationsCounterDao notificationsCounterDao;

    @Override
    public void sendMobileReadyAnnounce(List<Diver> divers) {
        new AnnounceTask<Diver>(mobileReadyMonitor, scheduler) {
            @Override
            protected boolean shouldAnnounce(Diver element) {
                NotificationsCounter notificationsCounter = notificationsCounterDao.getByDiver(element);
                return !notificationsCounter.isUnsubscribed()
                       && notificationsCounter.getMobileCnt() < MAX_MOBILE_EMAILS;
            }

            @Override
            protected void announce(Diver element) {
                NotificationsCounter notificationsCounter = notificationsCounterDao.getByDiver(element);
                notificationsCounter.setMobileCnt(notificationsCounter.getMobileCnt() + 1);
                String unsubscribeToken = Base64Coder.encodeString(
                        randomazer.generateRandomStringByUniqueId(
                                notificationsCounter.getId(), UNSUBSCRIBE_TOKEN_RAND_PART_LENGTH
                        )
                );
                notificationsCounter.setUnsubscribeToken(unsubscribeToken);
                notificationsCounterDao.updateModel(notificationsCounter);
                mailService.cmasMobileAnnounce(element, notificationsCounter);
            }
        }.schedule(divers);
    }

    @Override
    public void resendRegistrations(List<Registration> registrations) {
        new AnnounceTask<Registration>(registrationsMonitor, scheduler) {
            @Override
            protected boolean shouldAnnounce(Registration element) {
                return true;
            }

            @Override
            protected void announce(Registration element) {
                mailService.sendRegistration(element, true);
            }
        }.schedule(registrations);
    }

    @Override
    public void sendManualsToFederations(List<Diver> federationAdmins) {
        new AnnounceTask<Diver>(federationManualsMonitor, scheduler) {
            @Override
            protected boolean shouldAnnounce(Diver element) {
                NotificationsCounter notificationsCounter = notificationsCounterDao.getByDiver(element);
                return notificationsCounter.getFederationInitialCnt() < MAX_FEDERATION_MANUALS_EMAILS;
            }

            @Override
            protected void announce(Diver element) {
                NotificationsCounter notificationsCounter = notificationsCounterDao.getByDiver(element);
                notificationsCounter.setFederationInitialCnt(notificationsCounter.getFederationInitialCnt() + 1);
                notificationsCounterDao.updateModel(notificationsCounter);
                mailService.sendFederationManuals(element.getFederation(), element);
            }
        }.schedule(federationAdmins);
    }
}
