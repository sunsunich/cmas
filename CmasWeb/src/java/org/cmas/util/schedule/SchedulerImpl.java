package org.cmas.util.schedule;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@ManagedResource
public class SchedulerImpl implements DisposableBean, InitializingBean, Scheduler, ThreadFactory, Thread.UncaughtExceptionHandler {
    private final AtomicInteger threadNumber = new AtomicInteger(0);
    private ScheduledThreadPoolExecutor scheduler;
    private int numThreads = Runtime.getRuntime().availableProcessors() * 2;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final int TERMINATION_TIMEOUT_SEC = 60*2;

    @Override
    public boolean remove(Runnable task) {
        return scheduler.remove(task);
    }

    @Override
    public void purge() {
        scheduler.purge();
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return scheduler.schedule(command, delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return scheduler.schedule(callable, delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period) {
        return scheduler.scheduleAtFixedRate(command, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay) {
        return scheduler.scheduleWithFixedDelay(command, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    @NotNull
    @Override
    public Date scheduleDaily(@NotNull Runnable command, int hours, int minutes, TimeZone zone) {
        DailyScheduledTask task = new DailyScheduledTask(scheduler, command, (short)hours, (short)minutes, zone);
        return task.scheduledTime;
    }

    @NotNull
    @Override
    public Date scheduleWeekly(@NotNull Runnable command, int dayOfWeek, int hours, int minutes, TimeZone zone) {
        WeeklyScheduledTask task = new WeeklyScheduledTask(scheduler, command, (short)dayOfWeek, (short)hours, (short)minutes, zone);
        return task.scheduledTime;
    }

    @NotNull
    @Override
    public Date scheduleWeekly(@NotNull Runnable command, int dayOfWeek, int hours, int minutes) {
        return scheduleWeekly(command, dayOfWeek, hours, minutes, null);
    }

    @Override
    public void destroy() throws Exception {
        if (!scheduler.isTerminated()) {
            log.info("Shutting down scheduler");
            scheduler.shutdown();
            boolean success = scheduler.awaitTermination(TERMINATION_TIMEOUT_SEC, TimeUnit.SECONDS);
            if (!success) {
                log.error("Scheduler is not terminated after timeout period");
            }
            log.info("Scheduler is down");
        }
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    @Override
    public Thread newThread(Runnable r) {
        log.info("Making new scheduler thread for " + r.getClass());
        Thread t = new Thread(r, "Scheduler thr #" + threadNumber.incrementAndGet());
        //t.setUncaughtExceptionHandler();
        t.setDaemon(true);
        t.setPriority(Thread.MIN_PRIORITY);
        t.setUncaughtExceptionHandler(this);
        return t;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //scheduler = Executors.newScheduledThreadPool(numThreads, this);
        scheduler = new ScheduledThreadPoolExecutor(numThreads, this);
        scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Uncaught exception in scheduler task", e);
    }
}
