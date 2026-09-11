package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.function.Function;

public class SleeplessNightsFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Кол-во бессонных ночей", 0L);
        }

        LocalDate first = sessions.stream()
                .map(SleeplessNightsFunction::nightDate)
                .min(LocalDate::compareTo)
                .orElseThrow();

        LocalDate last = sessions.stream()
                .map(SleeplessNightsFunction::nightDate)
                .max(LocalDate::compareTo)
                .orElseThrow();

        long totalNights = Period.between(first, last).getDays() + 1;

        long sleptNights = sessions.stream()
                .filter(SleepingSession::isNightSession)
                .map(SleeplessNightsFunction::nightDate)
                .distinct()
                .count();

        long value = totalNights - sleptNights;

        return new SleepAnalysisResult("Кол-во бессонных ночей", value);
    }

    private static LocalDate nightDate(SleepingSession session) {
        if (session.getStart().toLocalTime().isAfter(LocalTime.NOON)) {
            return session.getStart().toLocalDate().plusDays(1);
        }
        return session.getStart().toLocalDate().minusDays(1);
    }
}