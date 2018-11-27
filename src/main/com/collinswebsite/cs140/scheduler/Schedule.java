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
