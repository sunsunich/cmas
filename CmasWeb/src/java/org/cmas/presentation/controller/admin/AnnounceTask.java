package org.cmas.presentation.controller.admin;

import org.cmas.util.schedule.Scheduler;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;

public abstract class AnnounceTask<T> implements Runnable {

    private final Object monitor;
    private final Scheduler scheduler;

    private ScheduledFuture<?> scheduledFuture;
    private Queue<T> elementsToProcess;

    protected AnnounceTask(Object monitor, Scheduler scheduler) {
        this.monitor = monitor;
        this.scheduler = scheduler;
    }

    public void schedule(Collection<T> elements) {
        synchronized (monitor) {
            elementsToProcess = new ArrayDeque<>(elements.size());
            for (T element : elements) {
                if (shouldAnnounce(element)) {
                    elementsToProcess.add(element);
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
            if (elementsToProcess.isEmpty()) {
                scheduledFuture.cancel(false);
            } else {
                T element = elementsToProcess.poll();
                if (shouldAnnounce(element)) {
                    announce(element);
                }
            }
        }
    }

    protected abstract boolean shouldAnnounce(T element);

    protected abstract void announce(T element);
}
