package com.collinswebsite.cs140.scheduler;

import java.util.*;

/**
 * Represents a specific offering of a given course.
 */
public class Section {
    /**
     * Represents the different types of courses.
     */
    public enum Type {
        /**
         * A standard, in-person class.
         */
        STANDARD("Standard"),
        /**
         * A class featuring both in-person and online components.
         */
        HYBRID("Hybrid"),
        /**
         * A class that is almost entirely online.
         */
        WCC_ONLINE("WCC online"),
        /**
         * A class that is almost entirely online.
         */
        WAOL_ONLINE("WAOL online"),
        /**
         * A class that is linked with another class.
         */
        LINKED("Linked"),
        /**
         * A class that involves studying in another country.
         */
        STUDY_ABROAD("Study abroad"),
        /**
         * Integrated Basic Education Skills and Training course.
         */
        IBEST("IBEST"),
        /**
         * None of the above.
         */
        UNKNOWN("Unknown");

        private final String name;
        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Type getByName(String name) {
            return Arrays.stream(Type.values()).filter((t) -> t.getName().equals(name)).findFirst().orElse(Type.UNKNOWN);
        }
    }

    private final Course course;
    private final int lineNo;
    private final String id;
    private final Type type;
    private final Map<Weekday, Set<TimeBlock>> meetingTimes; // set used here for automatic sorting

    public Section(Course course, int lineNo, String id, Type type) {
        this.meetingTimes = new EnumMap<>(Weekday.class);
        Arrays.stream(Weekday.values()).forEach((wk) -> meetingTimes.put(wk, new TreeSet<>()));

        this.course = course;
        this.lineNo = lineNo;
        this.id = id;
        this.type = type;
    }

    /**
     * @return The course that this section is offering
     */
    public Course getCourse() {
        return course;
    }

    /**
     * @return This section's line number, for registration
     */
    public int getLineNumber() {
        return lineNo;
    }

    /**
     * @return This section's alphanumeric ID ("A", "B", "HY1", etc.)
     */
    public String getId() {
        return id;
    }

    /**
     * @return This section's type
     */
    public Type getType() {
        return type;
    }

    /**
     * @param weekday Weekday to add this time block to
     * @param timeBlock Time this class meets on that weekday
     * Adds a meeting time to this section on the given weekday.
     */
    public void addTime(Weekday weekday, TimeBlock timeBlock) {
        Set<TimeBlock> set = meetingTimes.get(weekday);

        /*
          When a class meets in two rooms, it has a schedule that looks like this:
              TWF 09:30A-10:55A
              Th 09:30A-11:20A
              Th 09:30A-11:20A
          Some classes have schedules that look like this:
              TWThF 11:30A-12:50P
              F 11:30A-01:30P
          This is very confusing. I will just extend the schedule to encompass both blocks, I guess.
         */
        Optional<TimeBlock> overlap = set.stream().filter((tb) -> tb.overlaps(timeBlock)).findFirst();
        if(overlap.isPresent()) {
            set.remove(overlap.get());
            set.add(overlap.get().extend(timeBlock));
        } else {
            meetingTimes.get(weekday).add(timeBlock);
        }
    }

    public Set<TimeBlock> getTimes(Weekday weekday) {
        return meetingTimes.get(weekday);
    }

    public String toString() {
        return lineNo + " " + course.getId() + " \"" + course.getTitle() + "\" " + id;
    }

    public String formatShort() {
        return course.getId() + " " + id;
    }
}
