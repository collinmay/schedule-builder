package com.collinswebsite.cs140.scheduler;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the parameters for schedule generation and sorting, as well as being a factory for schedule streams.
 */
public class Parameters {
    private final List<List<Section>> selectedSections;
    private Comparator<Schedule> comparator;

    public Parameters(Set<Course> selectedCourses, Comparator<Schedule> comparator) {
        this.selectedSections = selectedCourses.stream().map(Course::getSections).collect(Collectors.toList());
        this.comparator = comparator;
    }

    public Stream<Schedule> produceStream() {
        return Schedule.generateCombinations(selectedSections).filter(Schedule::isValid).sorted(comparator);
    }
}
