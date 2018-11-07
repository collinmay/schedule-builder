package com.collinswebsite.cs140.scheduler;

import com.collinswebsite.cs140.scheduler.dataproviders.DataProvider;
import com.collinswebsite.cs140.scheduler.dataproviders.DataRetrievalException;
import com.collinswebsite.cs140.scheduler.dataproviders.ScheduleSearchScraper;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Collection;
import java.util.Map;

public class SchedulerApplication {
    public static void main(String[] args) throws UnirestException, DataRetrievalException {
        DataProvider dataProvider = new ScheduleSearchScraper();
        Map<String, Course> courses = dataProvider.getCourseList();

        String requestedCourses[] = {"CS 145", "ENGL& 101", "PHYS& 221"};
        for(String requestedCourse : requestedCourses) {
            String normalized = Course.normalizeCourseId(requestedCourse);
            System.out.println("Requested " + requestedCourse + " ( -> " + normalized + "):");
            for(Section s : courses.get(normalized).getSections()) {
                System.out.println("  found " + s);
            }
        }
    }
}
