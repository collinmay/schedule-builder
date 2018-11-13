package com.collinswebsite.cs140.scheduler;

/**
 * Represents a specific offering of a given course.
 */
public class Section {
    private final Course course;
    private final int lineNo;
    private final String id;

    public Section(Course course, int lineNo, String id) {
        this.course = course;
        this.lineNo = lineNo;
        this.id = id;
    }

    /**
     * @return The course that this section is offering
     */
    public Course getCourse() {
        return course;
    }

    /**
     * @return This section's line number, for registration
     */
    public int getLineNumber() {
        return lineNo;
    }

    /**
     * @return This section's alphanumeric ID ("A", "B", "HY1", etc.)
     */
    public String getId() {
        return id;
    }

    public String toString() {
        return "Section[" + lineNo + " " + course.getId() + " \"" + course.getTitle() + "\" " + id + "]";
    }
}
