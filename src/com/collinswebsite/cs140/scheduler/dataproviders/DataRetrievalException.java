package com.collinswebsite.cs140.scheduler.dataproviders;

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
