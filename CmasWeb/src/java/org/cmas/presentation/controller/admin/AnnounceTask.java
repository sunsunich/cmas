package org.cmas.presentation.controller.admin;

import org.cmas.entities.diver.Diver;
import org.cmas.util.schedule.Scheduler;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;

public abstract class AnnounceTask implements Runnable {

    private final Object monitor;
    private final Scheduler scheduler;

    private ScheduledFuture<?> scheduledFuture;
    private Queue<Diver> diversToProcess;

    protected AnnounceTask(Object monitor, Scheduler scheduler) {
        this.monitor = monitor;
        this.scheduler = scheduler;
    }

    public void schedule(Collection<Diver> divers) {
        synchronized (monitor) {
            diversToProcess = new ArrayDeque<>(divers.size());
            for (Diver diver : divers) {
                if (shouldAnnounce(diver)) {
                    diversToProcess.add(diver);
                }
            }
            scheduledFuture = scheduler.scheduleWithFixedDelay(this,
                                                               0L,
                                                               UserAnnouncesServiceImpl.EMAIL_SEND_INTERVAL);
        }
    }

    @Override
    public void run() {
        synchronized (monitor) {
            if (diversToProcess.isEmpty()) {
                scheduledFuture.cancel(false);
            } else {
                Diver diver = diversToProcess.poll();
                if (shouldAnnounce(diver)) {
                    announce(diver);
                }
            }
        }
    }

    protected abstract boolean shouldAnnounce(Diver diver);

    protected abstract void announce(Diver diver);
}
