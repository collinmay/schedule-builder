package com.collinswebsite.cs140.scheduler;

import com.collinswebsite.cs140.scheduler.dataproviders.DataProvider;
import com.collinswebsite.cs140.scheduler.dataproviders.DataRetrievalException;
import com.collinswebsite.cs140.scheduler.dataproviders.ScheduleSearchScraper;
import com.collinswebsite.cs140.scheduler.view.CourseChooserView;
import com.collinswebsite.cs140.scheduler.view.tui.TuiCourseChooserView;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class SchedulerApplication {
    public static void main(String[] args) throws DataRetrievalException {
        DataProvider dataProvider = new ScheduleSearchScraper();
        Map<String, Course> courses = dataProvider.getCourseList();

        CourseChooserView chooser = new TuiCourseChooserView();
        Set<Course> selectedCourses = chooser.chooseCourses(courses);
        Schedule.generateCombinations(selectedCourses).filter(Schedule::isValid).sorted(Comparator.comparingInt(Schedule::calculateDeadTime)).limit(10).forEach((s) -> {
            System.out.println(s.toString() + " -> " + s.calculateDeadTime() / 60.0 + " dead hours");
        });
    }
}
