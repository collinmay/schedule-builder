package com.collinswebsite.cs140.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents an available course.
 */
public class Course {
    private final String id;
    private final String title;
    private final List<Section> sections;

    /**
     * @param id ID of the course, as in "MATH& 163" or "CS 140"
     * @param title Title of the course, as in "CALCULUS III" or "INTRO TO CS"
     */
    public Course(String id, String title) {
        this.id = id;
        this.title = title;
        this.sections = new ArrayList<>();
    }

    /**
     * @param id Input course ID
     * @return A version of the course ID adjusted to remove undesired distinctions.
     * Removes spaces and makes all letters capital so that "math & 163" and "MATH& 163"
     * both become "MATH&163".
     */
    public static String normalizeCourseId(String id) {
        return id.replaceAll(" ", "");
    }

    /**
     * @return ID of the course, as in "MATH& 163" or "CS 140"
     */
    public String getId() {
        return id;
    }

    /**
     * @return Title of the course, as in "CALCULUS III" or "INTRO TO CS"
     */
    public String getTitle() {
        return title;
    }

    /**
     * Adds a section offering to this course object. The section's course field should match this course.
     * @param section Section of this course to add
     */
    public void addSection(Section section) {
        sections.add(section);
    }

    /**
     * @return A collection of the various different sections for this course.
     */
    public Collection<Section> getSections() {
        return sections;
    }
}
