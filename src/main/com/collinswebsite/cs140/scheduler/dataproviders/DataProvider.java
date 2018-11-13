package com.collinswebsite.cs140.scheduler.dataproviders;

import com.collinswebsite.cs140.scheduler.Course;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * An abstract interface representing a strategy for obtaining lists of courses and sections.
 */
public interface DataProvider {
    /**
     * @return A map of normalized course IDs to available courses
     * @throws DataRetrievalException
     */
    public Map<String, Course> getCourseList() throws DataRetrievalException;

}
