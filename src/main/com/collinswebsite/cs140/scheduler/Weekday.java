package com.collinswebsite.cs140.scheduler;

import java.util.Arrays;

/**
 * Represents a day of the week.
 */
public enum Weekday {
    MONDAY("M", "Monday"),
    TUESDAY("T", "Tuesday"),
    WEDNESDAY("W", "Wednesday"),
    THURSDAY("Th", "Thursday"),
    FRIDAY("F", "Friday"),
    SATURDAY("Sa", "Saturday"),
    SUNDAY("Su", "Sunday");

    private final String shortName;
    private final String longName;

    Weekday(String shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public static Weekday findByShortName(String shortName) {
        return Arrays.stream(values()).filter((w) -> w.getShortName().equals(shortName)).findFirst().orElseThrow(NoSuchWeekdayError::new);
    }

    /**
     * @return A short (1 or 2 character) representation of the weekday (MTWThFSaSu)
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @return The full name of the weekday
     */
    public String getLongName() {
        return longName;
    }

    /**
     * @return An array containing Monday through Friday, but neither Saturday nor Sunday
     */
    public static Weekday[] getWeekdays() {
        return new Weekday[] {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY};
    }

    public static class NoSuchWeekdayError extends RuntimeException {
    }
}
