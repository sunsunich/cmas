package org.cmas.presentation.controller.admin;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.NotificationsCounter;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.dao.user.sport.NotificationsCounterDao;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.Base64Coder;
import org.cmas.util.random.Randomazer;
import org.cmas.util.schedule.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserAnnouncesServiceImpl implements UserAnnouncesService {

    private static final long EMAIL_SEND_INTERVAL = 1000L * 60L * 5L; // every 5 mins

    private static final int UNSUBSCRIBE_TOKEN_RAND_PART_LENGTH = 7;
    private static final int MAX_MOBILE_EMAILS = 1;
    private final Object mobileReadyMonitor = new Object();
    private final AtomicLong mobileReadyTaskId = new AtomicLong();
    private final Map<Long, ScheduledFuture<?>> mobileReadyTasks = new ConcurrentHashMap<>();

    private static final int MAX_FEDERATION_MANUALS_EMAILS = 1;
    private final Object federationManualsMonitor = new Object();
    private final AtomicLong federationManualsTaskId = new AtomicLong();
    private final Map<Long, ScheduledFuture<?>> federationManualsTasks = new ConcurrentHashMap<>();

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private MailService mailService;

    @Autowired
    private Randomazer randomazer;

    @Autowired
    private NotificationsCounterDao notificationsCounterDao;

    @Autowired
    private DiverDao diverDao;

    @Override
    public void sendMobileReadyAnnounce(final List<Diver> divers) {
        final AtomicInteger index = new AtomicInteger(0);
        final long newTaskId = mobileReadyTaskId.incrementAndGet();
        ScheduledFuture<?> mobileReadyTask = scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                synchronized (mobileReadyMonitor) {
                    if (index.get() < divers.size()) {
                        Diver diver = divers.get(index.getAndIncrement());
                        NotificationsCounter notificationsCounter = notificationsCounterDao.getByDiver(diver);
                        int mobileCnt = notificationsCounter.getMobileCnt();
                        if (!notificationsCounter.isUnsubscribed() && mobileCnt < MAX_MOBILE_EMAILS) {
                            notificationsCounter.setMobileCnt(mobileCnt + 1);
                            String unsubscribeToken = Base64Coder.encodeString(
                                    randomazer.generateRandomStringByUniqueId(
                                            notificationsCounter.getId(), UNSUBSCRIBE_TOKEN_RAND_PART_LENGTH
                                    )
                            );
                            notificationsCounter.setUnsubscribeToken(unsubscribeToken);
                            notificationsCounterDao.updateModel(notificationsCounter);
                            mailService.cmasMobileAnnounce(diver, notificationsCounter);
                        }
                    } else {
                        ScheduledFuture<?> scheduledFuture = mobileReadyTasks.remove(newTaskId);
                        if (scheduledFuture != null) {
                            scheduledFuture.cancel(false);
                        }
                    }
                }
            }
        }, 1000L, EMAIL_SEND_INTERVAL);
        mobileReadyTasks.put(newTaskId, mobileReadyTask);
    }

    @Override
    public void sendManualsToFederations(final List<Diver> federationAdmins) {
        final AtomicInteger index = new AtomicInteger(0);
        final long newTaskId = federationManualsTaskId.incrementAndGet();
        ScheduledFuture<?> federationManualsTask = scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                synchronized (federationManualsMonitor) {
                    if (index.get() < federationAdmins.size()) {
                        Diver federationAdmin = federationAdmins.get(index.getAndIncrement());
                        NotificationsCounter notificationsCounter = notificationsCounterDao.getByDiver(federationAdmin);
                        int federationInitialCnt = notificationsCounter.getFederationInitialCnt();
                        if (federationInitialCnt < MAX_FEDERATION_MANUALS_EMAILS) {
                            notificationsCounter.setFederationInitialCnt(federationInitialCnt + 1);
                            notificationsCounterDao.updateModel(notificationsCounter);
                            mailService.sendFederationManuals(federationAdmin.getFederation(), federationAdmin);
                        }
                    } else {
                        ScheduledFuture<?> scheduledFuture = federationManualsTasks.remove(newTaskId);
                        if (scheduledFuture != null) {
                            scheduledFuture.cancel(false);
                        }
                    }
                }
            }
        }, 1000L, EMAIL_SEND_INTERVAL);
        federationManualsTasks.put(newTaskId, federationManualsTask);
    }
}
