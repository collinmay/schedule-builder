package com.collinswebsite.cs140.scheduler;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {
    @Test
    void normalizeCourseId() {
        assertEquals(Course.normalizeCourseId("ABC DEF"), "ABCDEF");
        assertEquals(Course.normalizeCourseId(" a B  cd e F& 1 234"), "ABCDEF&1234");
    }

    @Test
    void getId() {
        assertEquals(new Course("course id", "title").getId(), "course id");
    }

    @Test
    void getTitle() {
        assertEquals(new Course("course id", "title").getTitle(), "title");
    }

    @Test
    void getSections() {
        Course c = new Course("TEST 101", "TEST COURSE");
        Section section = new Section(c, 0, "A");
        c.addSection(section);
        assertIterableEquals(c.getSections(), Collections.singleton(section));
    }
}