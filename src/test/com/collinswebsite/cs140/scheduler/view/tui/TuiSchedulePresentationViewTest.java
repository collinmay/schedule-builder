package com.collinswebsite.cs140.scheduler.view.tui;

import com.collinswebsite.cs140.scheduler.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class TuiSchedulePresentationViewTest {
    @Test
    void presentSchedule() {
        Course test101 = new Course("TEST101", "TEST COURSE 1");
        Course test102 = new Course("TEST102", "TEST COURSE 2");

        Section test101a = test101.addSection(new Section(test101, 1, "A", Section.Type.STANDARD));
        Section test102b = test102.addSection(new Section(test102, 4, "B", Section.Type.STANDARD));

        test101a.addTime(Weekday.MONDAY, new TimeBlock(test101a, 9, 45, 10, 50));
        test101a.addTime(Weekday.WEDNESDAY, new TimeBlock(test101a, 9, 45, 11, 50));
        test101a.addTime(Weekday.FRIDAY, new TimeBlock(test101a, 9, 45, 10, 50));


        test102b.addTime(Weekday.TUESDAY, new TimeBlock(test102b, 10, 15, 13, 0));
        test102b.addTime(Weekday.WEDNESDAY, new TimeBlock(test102b, 12, 0, 13, 0));

        Schedule schedule = new Schedule(Arrays.asList(test101a, test102b));

        new TuiSchedulePresentationView().presentSchedule(schedule, true);
    }
}