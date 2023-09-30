package ru.itmo.common.commands;

// энам с переменными-командами. Для каждой команды определены методы, которые нужно вызывать для неё.
public enum CommandType {
    ADD(new String[]{"askName", "askSoundtrackName", "askMinutesOfWaiting",
            "askImpactSpeed", "askRealHero", "askHasToothpick", "askCoordinates", "askMood", "askCar"}),
    CLEAR(new String[]{}),
    EXIT(new String[]{}),
    FILTER_BY_MINUTES_OF_WAITING(new String[]{"askMinutesOfWaiting"}),
    FILTER_GREATER_THAN_IMPACT_SPEED(new String[]{"askImpactSpeed"}),
    HEAD(new String[]{}),
    HELP(new String[]{}),
    INFO(new String[]{}),
    PRINT_UNIQUE_IMPACT_SPEED(new String[]{}),
    REGISTRATION(new String[]{}),
    REMOVE_BY_ID(new String[]{"askId"}),
    REMOVE_GREATER(new String[]{}),
    REMOVE_HEAD(new String[]{}),
    SHOW(new String[]{}),
    UPDATE(new String[]{"askId", "askName", "askSoundtrackName", "askMinutesOfWaiting",
            "askImpactSpeed", "askRealHero", "askHasToothpick", "askCoordinates", "askMood", "askCar"}),
    CHECK_USER(new String[]{});


    private final String[] commandFields;

    CommandType(String[] fields) {
        this.commandFields = fields;
    }

    public String[] getCommandFields() {
        return this.commandFields;
    }
};