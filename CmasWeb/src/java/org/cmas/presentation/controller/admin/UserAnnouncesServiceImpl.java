package org.cmas.presentation.controller.admin;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.NotificationsCounter;
import org.cmas.presentation.dao.user.sport.NotificationsCounterDao;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.Base64Coder;
import org.cmas.util.random.Randomazer;
import org.cmas.util.schedule.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserAnnouncesServiceImpl implements UserAnnouncesService {

    static final long EMAIL_SEND_INTERVAL = 1000L ;//* 60L * 5L; // every 5 mins

    private static final int UNSUBSCRIBE_TOKEN_RAND_PART_LENGTH = 7;
    private static final int MAX_MOBILE_EMAILS = 1;
    private final Object mobileReadyMonitor = new Object();

    private static final int MAX_FEDERATION_MANUALS_EMAILS = 1;
    private final Object federationManualsMonitor = new Object();

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
        new AnnounceTask(mobileReadyMonitor, scheduler) {
            @Override
            protected boolean shouldAnnounce(Diver diver) {
                NotificationsCounter notificationsCounter = notificationsCounterDao.getByDiver(diver);
                return !notificationsCounter.isUnsubscribed()
                       && notificationsCounter.getMobileCnt() < MAX_MOBILE_EMAILS;
            }

            @Override
            protected void announce(Diver diver) {
                NotificationsCounter notificationsCounter = notificationsCounterDao.getByDiver(diver);
                notificationsCounter.setMobileCnt(notificationsCounter.getMobileCnt() + 1);
                String unsubscribeToken = Base64Coder.encodeString(
                        randomazer.generateRandomStringByUniqueId(
                                notificationsCounter.getId(), UNSUBSCRIBE_TOKEN_RAND_PART_LENGTH
                        )
                );
                notificationsCounter.setUnsubscribeToken(unsubscribeToken);
                notificationsCounterDao.updateModel(notificationsCounter);
                mailService.cmasMobileAnnounce(diver, notificationsCounter);
            }
        }.schedule(divers);
    }

    @Override
    public void sendManualsToFederations(List<Diver> federationAdmins) {
        new AnnounceTask(federationManualsMonitor, scheduler) {
            @Override
            protected boolean shouldAnnounce(Diver diver) {
                NotificationsCounter notificationsCounter = notificationsCounterDao.getByDiver(diver);
                return notificationsCounter.getFederationInitialCnt() < MAX_FEDERATION_MANUALS_EMAILS;
            }

            @Override
            protected void announce(Diver diver) {
                NotificationsCounter notificationsCounter = notificationsCounterDao.getByDiver(diver);
                notificationsCounter.setFederationInitialCnt(notificationsCounter.getFederationInitialCnt() + 1);
                notificationsCounterDao.updateModel(notificationsCounter);
                mailService.sendFederationManuals(diver.getFederation(), diver);
            }
        }.schedule(federationAdmins);
    }
}
