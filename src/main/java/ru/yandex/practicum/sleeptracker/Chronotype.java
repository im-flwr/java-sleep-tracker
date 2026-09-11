package ru.yandex.practicum.sleeptracker;

public enum Chronotype {
    OWL("сова"),
    LARK("жаворонок"),
    PIGEON("голубь");

    private final String title;

    Chronotype(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
