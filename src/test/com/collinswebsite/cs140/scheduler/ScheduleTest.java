package com.collinswebsite.cs140.scheduler;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {
    private Schedule generateTestSchedule() {
        Course test101 = new Course("TEST101", "TEST COURSE 1");
        Course test102 = new Course("TEST102", "TEST COURSE 2");

        Section test101a = test101.addSection(new Section(test101, 1, "A", Section.Type.STANDARD));
        Section test102b = test102.addSection(new Section(test102, 4, "B", Section.Type.STANDARD));

        test101a.addTime(Weekday.MONDAY, new TimeBlock(test101a, 9, 45, 10, 50));
        test101a.addTime(Weekday.WEDNESDAY, new TimeBlock(test101a, 9, 45, 11, 50));
        test101a.addTime(Weekday.FRIDAY, new TimeBlock(test101a, 9, 45, 10, 50));

        test102b.addTime(Weekday.TUESDAY, new TimeBlock(test102b, 10, 15, 13, 0));
        test102b.addTime(Weekday.WEDNESDAY, new TimeBlock(test102b, 12, 0, 13, 0));

        return new Schedule(Arrays.asList(test101a, test102b));
    }

    @Test
    void timesOn() {
        Schedule sched = generateTestSchedule();
        assertEquals(1, sched.timesOn(Weekday.MONDAY).count());
        assertEquals(1, sched.timesOn(Weekday.TUESDAY).count());
        assertEquals(2, sched.timesOn(Weekday.WEDNESDAY).count());
        assertEquals(0, sched.timesOn(Weekday.THURSDAY).count());
        assertEquals(1, sched.timesOn(Weekday.FRIDAY).count());
    }

    @Test
    void calculateDeadTime() {
        Schedule sched = generateTestSchedule();
        assertEquals(10, sched.calculateDeadTime());
    }

    @Test
    void numberOfDays() {
        assertEquals(4, generateTestSchedule().numberOfDays());
    }

    @Test
    void morningTax() {
        assertEquals(6, generateTestSchedule().morningTax());
    }

    @Test
    void isValid() {
        Course test101 = new Course("TEST101", "TEST COURSE 1");
        Course test102 = new Course("TEST102", "TEST COURSE 2");

        Section test101a = test101.addSection(new Section(test101, 1, "A", Section.Type.STANDARD));
        Section test102b = test102.addSection(new Section(test102, 4, "B", Section.Type.STANDARD));

        test101a.addTime(Weekday.MONDAY, new TimeBlock(test101a, 9, 45, 10, 50));
        test101a.addTime(Weekday.WEDNESDAY, new TimeBlock(test101a, 9, 45, 11, 50));
        test101a.addTime(Weekday.FRIDAY, new TimeBlock(test101a, 9, 45, 10, 50));

        test102b.addTime(Weekday.TUESDAY, new TimeBlock(test102b, 10, 15, 13, 0));
        test102b.addTime(Weekday.WEDNESDAY, new TimeBlock(test102b, 11, 0, 13, 0));

        assertFalse(new Schedule(Arrays.asList(test101a, test102b)).isValid());
        assertTrue(generateTestSchedule().isValid());
    }

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
