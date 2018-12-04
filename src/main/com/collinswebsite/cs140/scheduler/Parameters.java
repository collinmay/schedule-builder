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
    public double weightDeadTime = 1.0;
    public double weightMorningTax = 1.0;
    public double weightNumberOfDays = 300.0; // an extra day is worth a lot of dead time (split across the other days though)
    // Future metrics:
    //   - Evening tax
    //   - Reduce dead time weight for periods longer than two hours
    //   - Day efficiency (no days with only one class)

    public Parameters(Set<Course> selectedCourses) {
        this.selectedSections = selectedCourses.stream().map(Course::getSections).collect(Collectors.toList());
    }

    /**
     * @return A stream of valid schedules in order by the current parameters
     */
    public Stream<Schedule> produceStream() {
        return Schedule
                .generateCombinations(selectedSections)
                .filter(Schedule::isValid)
                .sorted(Comparator.comparingDouble((schedule) ->
                                schedule.calculateDeadTime() * weightDeadTime +
                                schedule.morningTax() * weightMorningTax +
                                schedule.numberOfDays() * weightNumberOfDays));
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
