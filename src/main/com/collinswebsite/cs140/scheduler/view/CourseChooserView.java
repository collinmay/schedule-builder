package com.collinswebsite.cs140.scheduler.view;

import com.collinswebsite.cs140.scheduler.Course;

import java.util.Map;
import java.util.Set;

/**
 * An interface for asking the user to choose which courses they are interested in.
 */
public interface CourseChooserView {
    /**
     * Asks the user to choose some of the available courses.
     * @param courses The available courses
     * @return The courses that the user selected
     */
    public Set<Course> chooseCourses(Map<String, Course> courses);
}
