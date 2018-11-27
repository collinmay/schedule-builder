package com.collinswebsite.cs140.scheduler.view;

import com.collinswebsite.cs140.scheduler.Parameters;

/**
 * An interface for presenting schedules to the user and allowing them to tweak generation parameters.
 */
public interface SchedulePresentationView {
    /**
     * @param parameters Initial schedule generation parameters. May be mutated.
     */
    public void present(Parameters parameters);
}
