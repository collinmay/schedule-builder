package com.collinswebsite.cs140.scheduler.view;

import com.collinswebsite.cs140.scheduler.Course;

import java.util.Map;
import java.util.Set;

public interface CourseChooserView {
    public Set<Course> chooseCourses(Map<String, Course> courses);
}
