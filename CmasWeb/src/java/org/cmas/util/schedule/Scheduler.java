package org.cmas.util.schedule;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface Scheduler {

     boolean remove(Runnable task);

    ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit);

    <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

    /**
     * Запускает периодическое выполнение команды с фиксированной частотой выполнения.
     * @see {@link java.util.concurrent.ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, java.util.concurrent.TimeUnit)}
     * @param command Команда
     * @param initialDelay Задержка от текущего моента до первого выполнения, милисекунд
     * @param period Интервал между выполнениями в милисекнудах
     */
    ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period);

    /**
     * Запускает ежедневное выполнение команды
     * @param command Команда
     * @param hours Время запуска, в часах
     * @param minutes Время запуска, в минутах
     * @param zone Временная зона, к которой относятся временные параметры
     * @return Время, когда команда запустится в первый раз
     */
    @NotNull
    Date scheduleDaily(@NotNull Runnable command, int hours, int minutes, @Nullable TimeZone zone);

    /**
     * Запускает ежедневное выполнение команды
     * @param command Команда
     * @param hours Время запуска, в часах
     * @param minutes Время запуска, в минутах
     * @return Время, когда команда запустится в первый раз
     */
    @NotNull
    Date scheduleDaily(@NotNull Runnable command, int hours, int minutes);

    @NotNull
    Date scheduleWeekly(@NotNull Runnable command, int dayOfWeek, int hours, int minutes, TimeZone zone);

    @NotNull
    Date scheduleWeekly(@NotNull Runnable command, int dayOfWeek, int hours, int minutes);

    /**
     * Запускает периодическое выполнение команды c фиксрованной задержкой между выполненями.
     * @see {@link java.util.concurrent.ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, java.util.concurrent.TimeUnit)}
     * @param command Команда
     * @param initialDelay Задержка от текущего моента до первого выполнения, милисекунд
     * @param delay Интервал между выполнениями в милисекнудах
     */
    ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay);
}
