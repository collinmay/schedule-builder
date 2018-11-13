package com.collinswebsite.cs140.scheduler.dataproviders;

/**
 * Represents a generic error that occurred while retrieving a course list.
 */
public class DataRetrievalException extends Exception {
    public DataRetrievalException(Exception e) {
        super(e);
    }
    public DataRetrievalException(String s) {
        super(s);
    }
    public DataRetrievalException() {

    }
}
