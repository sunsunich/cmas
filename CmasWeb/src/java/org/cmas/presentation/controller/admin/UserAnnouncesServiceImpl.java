package org.cmas.presentation.controller.admin;

import org.cmas.entities.diver.Diver;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.schedule.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UserAnnouncesServiceImpl implements UserAnnouncesService{

    private static final long EMAIL_SEND_INTERVAL = 1000L * 60L * 5L; // every 5 mins

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private MailService mailService;

    @Override
    public void sendMobileReadyAnnounce(List<Diver> divers) {
        AtomicInteger index = new AtomicInteger(0);
        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Diver diver = divers.get(index.getAndIncrement());
                mailService.cmasMobileAnnounce(diver);
            }
        }, 0L, EMAIL_SEND_INTERVAL);
    }
}
