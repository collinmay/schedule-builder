package com.collinswebsite.cs140.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents a possible schedule
 */
public class Schedule {
    private final Set<Section> sections;

    public Schedule(Set<Section> sections) {
        this.sections = sections;
    }

    public Schedule(List<Section> sections) {
        this.sections = new HashSet<>(sections);
    }

    /**
     * @param courses Sections to generate schedules with, grouped by course
     * @return A stream of all possible combinations of sections between the given courses
     */
    public static Stream<Schedule> generateCombinations(List<List<Section>> courseList) {
        int numCombinations = courseList.stream().mapToInt((c) -> c.size()).reduce(1, (a, b) -> a * b);
        return IntStream.range(0, numCombinations).mapToObj((index) -> {
           Set<Section> sectionList = new HashSet<>(courseList.size());
           while(sectionList.size() < courseList.size()) {
               List<Section> c = courseList.get(sectionList.size());
               sectionList.add(c.get(index % c.size()));
               index/= c.size();
           }
           return new Schedule(sectionList);
        });
    }

    /**
     * @return The sections that this schedule is composed of
     */
    public Set<Section> getSections() {
        return sections;
    }

    public String toString() {
        return sections.toString();
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Schedule) {
            return sections.equals(((Schedule) other).sections);
        } else {
            return false;
        }
    }

    public Stream<TimeBlock> timesOn(Weekday weekday) {
        return sections.stream().flatMap((section) -> section.getTimes(weekday).stream()).sorted();
    }

    public int calculateDeadTime() {
        return Stream.of(Weekday.values()).mapToInt((weekday) -> {
            List<TimeBlock> times = timesOn(weekday).collect(Collectors.toList());
            if(times.size() <= 1) {
                return 0;
            }
            int deadMinutes = 0;
            TimeBlock last = times.get(0);
            for(int i = 1; i < times.size(); i++) {
                deadMinutes+= last.minutesBetween(times.get(i));
                last = times.get(i);
            }
            return deadMinutes;
        }).sum();
    }

    public int numberOfDays() {
        return Stream.of(Weekday.values()).mapToInt((weekday) -> timesOn(weekday).count() > 0 ? 1 : 0).sum();
    }

    public int morningTax() {
        return (int) Stream.of(Weekday.values()).flatMapToDouble((weekday) -> timesOn(weekday).mapToDouble((tb) -> {
            double tax = 0;
            // poor man's definite integral; apply a tax for every minute before 10:00, with
            // higher taxes the earlier the minute is.
            // The minute at 10:00 has zero tax, and the minute at 7:00 counts as three dead minutes.
            // That's one minute for every hour before 10:00.

            // If the class ends before 10:00, we also count the time between when it ends and 10:00
            // since that's still time I could've spent sleeping.

            int threshold = 10 * 60; // 10:00
            double deadMinutesPerHour = 1.0;
            for(int t = tb.getBeginHour() * 60 + tb.getBeginMinute(); t < threshold; t++) {
                tax+= deadMinutesPerHour * (threshold - t) / 60.0;
            }
            return tax;
        })).sum();
    }

    public boolean isValid() {
        return Stream.of(Weekday.values()).allMatch((weekday) -> {
           List<TimeBlock> times = timesOn(weekday).collect(Collectors.toList());
            if(times.size() <= 1) {
                return true; // no conflicts possible
            }
            TimeBlock last = times.get(0);
            for(int i = 1; i < times.size(); i++) {
                if(last.overlaps(times.get(i))) {
                    return false; // overlap conflict
                }
                last = times.get(i);
            }
            return true; // no conflict detected
        });
    }
}
