package com.collinswebsite.cs140.scheduler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeekdayTest {

    @Test
    void findByShortName() {
        assertEquals(Weekday.findByShortName("M"), Weekday.MONDAY);
        assertEquals(Weekday.findByShortName("T"), Weekday.TUESDAY);
        assertEquals(Weekday.findByShortName("W"), Weekday.WEDNESDAY);
        assertEquals(Weekday.findByShortName("Th"), Weekday.THURSDAY);
        assertEquals(Weekday.findByShortName("F"), Weekday.FRIDAY);
        assertEquals(Weekday.findByShortName("Sa"), Weekday.SATURDAY);
        assertEquals(Weekday.findByShortName("Su"), Weekday.SUNDAY);

        assertThrows(Weekday.NoSuchWeekdayError.class, () -> Weekday.findByShortName("bad"));
    }
}