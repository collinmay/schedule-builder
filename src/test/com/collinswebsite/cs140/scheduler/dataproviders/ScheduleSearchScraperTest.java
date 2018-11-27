package com.collinswebsite.cs140.scheduler.dataproviders;

import com.collinswebsite.cs140.scheduler.Course;
import com.collinswebsite.cs140.scheduler.Section;
import com.collinswebsite.cs140.scheduler.TimeBlock;
import com.collinswebsite.cs140.scheduler.Weekday;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduleSearchScraperTest {
    @Test
    void getCourseList() throws DataRetrievalException {
        ScheduleSearchScraper scraper = new ScheduleSearchScraper();
        Map<String, Course> courseMap = scraper.getCourseList();

        assertNotEquals(courseMap.size(), 0);
        courseMap.forEach((id, course) -> {
            assertEquals(id, Course.normalizeCourseId(id)); // make sure key has been normalized
            assertEquals(id, course.getId()); // make sure key matches course's ID field
        });
    }

    @Test
    void parseSection() throws DataRetrievalException {
        Section section = new ScheduleSearchScraper().parseSection(new HashMap<>(),
                Arrays.asList("123", "PARSER TEST COURSE", "TEST& 109 HY1", "3", "Open", "DAILY 09:45A-10:50A", "Standard", "TES 001", "MAY, C"));

        assertEquals(123, section.getLineNumber());
        assertEquals("PARSER TEST COURSE", section.getCourse().getTitle());
        assertEquals(Course.normalizeCourseId("TEST & 109"), section.getCourse().getId());
        assertEquals("HY1", section.getId());
        assertEquals(Section.Type.STANDARD, section.getType());

        for(Weekday wk : Weekday.getWeekdays()) {
            assertEquals(
                    Collections.singletonList(new TimeBlock(section, 9, 45, 10, 50)),
                    new ArrayList<>(section.getTimes(wk)));
        }
        assertTrue(section.getTimes(Weekday.SATURDAY).isEmpty());
        assertTrue(section.getTimes(Weekday.SUNDAY).isEmpty());
    }
}