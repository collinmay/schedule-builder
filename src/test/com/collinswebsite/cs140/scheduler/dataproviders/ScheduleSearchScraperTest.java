package com.collinswebsite.cs140.scheduler.dataproviders;

import com.collinswebsite.cs140.scheduler.Course;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
}