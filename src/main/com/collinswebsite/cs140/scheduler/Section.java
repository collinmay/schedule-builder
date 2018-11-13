package com.collinswebsite.cs140.scheduler;

public class Section {
    private final Course course;
    private final int lineNo;
    private final String id;

    public Section(Course course, int lineNo, String id) {
        this.course = course;
        this.lineNo = lineNo;
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public int getLineNumber() {
        return lineNo;
    }

    public String getId() {
        return id;
    }

    public String toString() {
        return "Section[" + lineNo + " " + course.getId() + " \"" + course.getTitle() + "\" " + id + "]";
    }
}
