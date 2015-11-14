package org.cmas.util.schedule;

import org.cmas.Globals;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//TODO refactor closer to DailyScheduledTask
class WeeklyScheduledTask implements Runnable {
    private final Logger log = LoggerFactory.getLogger(getClass());

    Date scheduledTime;
    final short dayOfWeek;
    final short hours;
    final short minutes;
    final Runnable target;
    final ScheduledThreadPoolExecutor scheduler;


    private void scrollToFuture() {
        // Дельта в 1 секунду на случай если вызвали раньше запланированного времени
        while (scheduledTime.getTime() - System.currentTimeMillis() < 1000) {
            scheduledTime = new Date(scheduledTime.getTime() + Globals.ONE_WEEK_IN_MS);
        }
    }

    private void reSchedule() {
        if (log.isDebugEnabled()) {
            log.debug("Next run is planned to " + scheduledTime);
        }
        long delay = scheduledTime.getTime() - System.currentTimeMillis();
        scheduler.schedule(this, delay, TimeUnit.MILLISECONDS);
    }

    WeeklyScheduledTask(ScheduledThreadPoolExecutor scheduler, Runnable target
            , short dayOfWeek, short hours, short minutes
            , @Nullable TimeZone zone
    ) {
        this.scheduler = scheduler;
        this.dayOfWeek = dayOfWeek;
        this.target = target;
        this.hours = hours;
        this.minutes = minutes;
        Calendar cal = Calendar.getInstance(zone);
        cal.set(Calendar.DAY_OF_WEEK, (int) dayOfWeek);
        cal.set(Calendar.HOUR_OF_DAY, (int) hours);
        cal.set(Calendar.MINUTE, (int) minutes);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        scheduledTime = cal.getTime();
        scrollToFuture();
        reSchedule();
    }

    @Override
    public void run() {
        log.info("Running scheduled task " + target.getClass());
        // Смотрим не сдвинулось ли время при предыдущем планировании - например если был перевод времени
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(scheduledTime);
        boolean needAdjust = calendar.get(Calendar.HOUR_OF_DAY) != hours
                          || calendar.get(Calendar.MINUTE) != minutes
                ;

        scrollToFuture();
        if (needAdjust) {
            log.info("Adjusting scheduling time from " + scheduledTime);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, (int) dayOfWeek);
            cal.set(Calendar.HOUR_OF_DAY, (int) hours);
            cal.set(Calendar.MINUTE, (int) minutes);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            scheduledTime = cal.getTime();
            log.info("Time adjusted to " + scheduledTime);
        }
        reSchedule();
        try {
            target.run();
        } catch (Exception e) {
            log.error("Error running scheduled task", e);
        }
    }
}
