package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {

    private final SleepingSession night = new SleepingSession(
            LocalDateTime.of(2025, 10, 1, 23, 0),
            LocalDateTime.of(2025, 10, 2, 7, 0),
            SleepQuality.GOOD);

    private final SleepingSession bad = new SleepingSession(
            LocalDateTime.of(2025, 10, 2, 23, 0),
            LocalDateTime.of(2025, 10, 3, 6, 0),
            SleepQuality.BAD);

    @Test
    void calculatesBasicMetrics() {
        List<SleepingSession> sessions = List.of(night, bad);
        assertEquals(2, new CountSleepingSessionsFunction().apply(sessions).getValue());
        assertEquals(420L, new MinSleepDurationFunction().apply(sessions).getValue());
        assertEquals(480L, new MaxSleepDurationFunction().apply(sessions).getValue());
        assertEquals(450.0, new AverageSleepDurationFunction().apply(sessions).getValue());
        assertEquals(1L, new BadSleepQualityFunction().apply(sessions).getValue());
    }

    @Test
    void calculatesChronotype() {
        assertEquals(
                Chronotype.PIGEON,
                new ChronotypeFunction().apply(List.of(night, bad)).getValue());

        SleepingSession owl = new SleepingSession(
                LocalDateTime.of(2025, 10, 3, 23, 30),
                LocalDateTime.of(2025, 10, 4, 10, 0),
                SleepQuality.GOOD);
        assertEquals(
                Chronotype.OWL,
                new ChronotypeFunction().apply(List.of(owl)).getValue());

        SleepingSession lark = new SleepingSession(
                LocalDateTime.of(2025, 10, 4, 21, 30),
                LocalDateTime.of(2025, 10, 5, 6, 0),
                SleepQuality.GOOD);
        assertEquals(
                Chronotype.LARK,
                new ChronotypeFunction().apply(List.of(lark)).getValue());

        SleepingSession pigeon = new SleepingSession(
                LocalDateTime.of(2025, 10, 5, 22, 30),
                LocalDateTime.of(2025, 10, 6, 7, 30),
                SleepQuality.GOOD);
        assertEquals(
                Chronotype.PIGEON,
                new ChronotypeFunction().apply(List.of(pigeon)).getValue());

        assertEquals(
                Chronotype.PIGEON,
                new ChronotypeFunction().apply(List.of(owl, lark)).getValue());
    }

    @Test
    void calculatesSleeplessNight() {
        SleepingSession day = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 12, 0),
                LocalDateTime.of(2025, 10, 2, 13, 0),
                SleepQuality.NORMAL);
        assertEquals(
                1L,
                new SleeplessNightsFunction().apply(List.of(night, day)).getValue());
    }

    @Test
    void handlesEmptyLog() {
        List<SleepingSession> sessions = List.of();
        assertEquals(0L, new SleeplessNightsFunction().apply(sessions).getValue());
        assertEquals(Chronotype.PIGEON, new ChronotypeFunction().apply(sessions).getValue());
    }
}