package ru.yandex.practicum.sleeptracker;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class SleeplessNightsFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) return new SleepAnalysisResult("Кол-во бессонных ночей", 0L);
        LocalDate first = sessions.stream()
                .map(SleeplessNightsFunction::nightDate)
                .min(LocalDate::compareTo)
                .orElseThrow();
        LocalDate last = sessions.stream()
                .map(SleeplessNightsFunction::nightDate)
                .max(LocalDate::compareTo)
                .orElseThrow();
        long value = LongStream.rangeClosed(0, ChronoUnit.DAYS.between(first, last))
                .mapToObj(first::plusDays)
                .filter(date -> sessions.stream()
                        .noneMatch(s -> nightDate(s).equals(date) && s.isNightSession()))
                .count();
        return new SleepAnalysisResult("Кол-во бессонных ночей", value);
    }

    private static LocalDate nightDate(SleepingSession session) {
        if (session.getStart().toLocalTime().isAfter(LocalTime.NOON)) {
            return session.getStart().toLocalDate().plusDays(1);
        }
        return session.getStart().toLocalDate().minusDays(1);
    }
}
