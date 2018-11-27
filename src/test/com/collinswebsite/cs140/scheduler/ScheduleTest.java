package com.collinswebsite.cs140.scheduler;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ScheduleTest {
    @Test
    void getSections() {
        Set<Section> sectionSet = new HashSet<>();
        sectionSet.add(new Section(new Course("abc", "def"), 0, "A", Section.Type.STANDARD));

        Schedule sch = new Schedule(sectionSet);
        assertEquals(sch.getSections(), sectionSet);
    }

    @Test
    void generateCombinations() {
        Course test101 = new Course("TEST101", "TEST COURSE 1");
        Course test102 = new Course("TEST102", "TEST COURSE 2");

        Section test101a = test101.addSection(new Section(test101, 1, "A", Section.Type.STANDARD));
        Section test101b = test101.addSection(new Section(test101, 2, "B", Section.Type.STANDARD));
        Section test102a = test102.addSection(new Section(test102, 3, "A", Section.Type.STANDARD));
        Section test102b = test102.addSection(new Section(test102, 4, "B", Section.Type.STANDARD));
        Section test102c = test102.addSection(new Section(test102, 5, "C", Section.Type.STANDARD));

        List<Course> courses = Arrays.asList(test101, test102);

        assertEquals(Arrays.asList(
                new Schedule(Arrays.asList(test101a, test102a)),
                new Schedule(Arrays.asList(test101b, test102a)),
                new Schedule(Arrays.asList(test101a, test102b)),
                new Schedule(Arrays.asList(test101b, test102b)),
                new Schedule(Arrays.asList(test101a, test102c)),
                new Schedule(Arrays.asList(test101b, test102c))
                ),
                Schedule.generateCombinations(courses.stream().map(Course::getSections).collect(Collectors.toList())).collect(Collectors.toList()));
    }

    @Test
    void equals() {
        Course test101 = new Course("TEST101", "TEST COURSE 1");
        Course test102 = new Course("TEST102", "TEST COURSE 2");

        Section test101a = test101.addSection(new Section(test101, 1, "A", Section.Type.STANDARD));
        Section test101b = test101.addSection(new Section(test101, 2, "B", Section.Type.STANDARD));
        Section test102a = test102.addSection(new Section(test102, 3, "A", Section.Type.STANDARD));

        assertEquals(new Schedule(Arrays.asList(test101a, test102a)), new Schedule(Arrays.asList(test101a, test102a)));
        assertEquals(new Schedule(Arrays.asList(test101a, test102a)), new Schedule(Arrays.asList(test102a, test101a))); // doesn't care about order

        assertNotEquals(new Schedule(Arrays.asList(test101a, test102a)), new Schedule(Arrays.asList(test101b, test102a)));
    }
}
