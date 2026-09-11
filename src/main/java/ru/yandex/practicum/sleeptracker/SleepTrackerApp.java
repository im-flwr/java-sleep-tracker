package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SleepTrackerApp {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private static final List<Function<List<SleepingSession>, SleepAnalysisResult>> FUNCTIONS = List.of(
            new CountSleepingSessionsFunction(), new MinSleepDurationFunction(),
            new MaxSleepDurationFunction(), new AverageSleepDurationFunction(),
            new BadSleepQualityFunction(), new SleeplessNightsFunction(), new ChronotypeFunction());

    public static void main(String[] args) {
        Path path;
        if (args.length == 0) {
            path = Path.of("src/main/resources/sleep_log.txt");
        } else {
            path = Path.of(args[0]);
        }
        List<SleepingSession> sessions = readSessions(path);
        if (sessions.isEmpty()) {
            return;
        }
        FUNCTIONS.stream().map(function -> function.apply(sessions)).forEach(System.out::println);
    }

    private static List<SleepingSession> readSessions(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            return lines.filter(line -> !line.isBlank()).map(SleepTrackerApp::parseSession).toList();
        } catch (IOException exception) {
            throw new IllegalArgumentException("Не удалось прочитать файл: " + path, exception);
        }
    }

    private static SleepingSession parseSession(String line) {
        String[] parts = line.trim().split(";");
        return new SleepingSession(LocalDateTime.parse(parts[0], FORMATTER), LocalDateTime.parse(parts[1], FORMATTER), SleepQuality.valueOf(parts[2].trim()));
    }
}
