package com.collinswebsite.cs140.scheduler;

import java.util.Collection;
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

    /**
     * @return A stream of valid schedules in order by the current parameters
     */
    public Stream<Schedule> produceStream() {
        return Schedule.generateCombinations(selectedSections).filter(Schedule::isValid).sorted(comparator);
    }

    /**
     * @param lineNumber Line number to look up section by
     * @return Section with the given line number, or null if none was found
     */
    public Section getSectionByLineNumber(int lineNumber) {
        return selectedSections.stream().flatMap(Collection::stream).filter((s) -> s.getLineNumber() == lineNumber).findFirst().orElse(null);
    }

    /**
     * Removes a specific section from consideration
     * @param section Section to remove
     */
    public void reject(Section section) {
        selectedSections.forEach((list) -> list.remove(section));
    }
}
