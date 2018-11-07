package com.collinswebsite.cs140.scheduler;

import com.collinswebsite.cs140.scheduler.dataproviders.DataRetrievalException;
import com.collinswebsite.cs140.scheduler.dataproviders.ScheduleSearchScraper;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SchedulerApplication {
    public static void main(String[] args) throws UnirestException, DataRetrievalException {
        new ScheduleSearchScraper().scrape(null);
    }
}
