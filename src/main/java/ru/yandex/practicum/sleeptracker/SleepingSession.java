package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SleepingSession {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime start, LocalDateTime end, SleepQuality quality) {
        this.start = start;
        this.end = end;
        this.quality = quality;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDurationMinutes() {
        return Duration.between(start, end).toMinutes();
    }

    public boolean isNightSession() {
        return !start.toLocalDate().equals(end.toLocalDate()) || start.toLocalTime().isBefore(LocalTime.of(6, 0));
    }
}
