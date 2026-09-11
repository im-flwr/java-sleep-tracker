package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream()
                .filter(SleepingSession::isNightSession)
                .map(this::classify)
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));

        long max = counts.isEmpty() ? 0 : Collections.max(counts.values());

        long ties = counts.values().stream()
                .filter(value -> value == max)
                .count();

        Chronotype result;
        if (max == 0 || ties > 1) {
            result = Chronotype.PIGEON;
        } else {
            result = counts.entrySet().stream()
                    .filter(entry -> entry.getValue() == max)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(Chronotype.PIGEON);
        }

        return new SleepAnalysisResult("Хронотип пользователя", result);
    }

    private Chronotype classify(SleepingSession session) {
        LocalTime start = session.getStart().toLocalTime();
        LocalTime end = session.getEnd().toLocalTime();

        if (start.isAfter(LocalTime.of(23, 0)) && end.isAfter(LocalTime.of(9, 0))) {
            return Chronotype.OWL;
        }
        if (start.isBefore(LocalTime.of(22, 0)) && end.isBefore(LocalTime.of(7, 0))) {
            return Chronotype.LARK;
        }
        return Chronotype.PIGEON;
    }
}