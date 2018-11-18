package com.collinswebsite.cs140.scheduler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeBlockTest {

    @Test
    void testToString() {
        assertEquals("04:53A-08:23A", new TimeBlock(4, 53, 8, 23).toString());
        assertEquals("04:53A-08:23P", new TimeBlock(4, 53, 20, 23).toString());
        assertEquals("12:53A-12:23P", new TimeBlock(0, 53, 12, 23).toString());
    }

    @Test
    void beginsBefore() {
        TimeBlock block = new TimeBlock(4, 53, 8, 23);
        assertTrue(block.beginsBefore(9, 23));
        assertTrue(block.beginsBefore(4, 54));
        assertFalse(block.beginsBefore(4, 53));
        assertFalse(block.beginsBefore(4, 52));
        assertFalse(block.beginsBefore(3, 30));
    }

    @Test
    void endsAfter() {
        TimeBlock block = new TimeBlock(4, 53, 8, 23);
        assertTrue(block.endsAfter(7, 23));
        assertTrue(block.endsAfter(8, 22));
        assertFalse(block.endsAfter(8, 23));
        assertFalse(block.endsAfter(8, 24));
        assertFalse(block.endsAfter(9, 15));
    }

    @Test
    void contains() {
        TimeBlock block = new TimeBlock(4, 53, 8, 23);
        // barely inside range
        assertTrue(block.contains(4, 54));
        assertTrue(block.contains(8, 22));

        // well inside range
        assertTrue(block.contains(6, 0));
        assertTrue(block.contains(6, 59));

        // barely outside range
        assertFalse(block.contains(4, 52));
        assertFalse(block.contains(8, 24));

        // well before range
        assertFalse(block.contains(3, 0));
        assertFalse(block.contains(3, 59));

        // well after range
        assertFalse(block.contains(9, 0));
        assertFalse(block.contains(9, 59));

        // boundaries
        assertTrue(block.contains(4, 53));
        assertTrue(block.contains(8, 23));
    }

    @Test
    void overlaps() {
        TimeBlock block = new TimeBlock(4, 53, 8, 23);

        assertTrue(block.overlaps(new TimeBlock(4, 0, 4, 53)));
        assertTrue(block.overlaps(new TimeBlock(4, 53, 8, 22)));
        assertTrue(block.overlaps(new TimeBlock(4, 53, 8, 24)));
        assertTrue(block.overlaps(new TimeBlock(4, 53, 11, 0)));
        assertTrue(block.overlaps(new TimeBlock(8, 23, 11, 0)));
        assertTrue(block.overlaps(new TimeBlock(2, 0, 11, 0)));
        assertTrue(block.overlaps(new TimeBlock(6, 0, 7, 0)));

        assertFalse(block.overlaps(new TimeBlock(1, 0, 2, 0)));
        assertFalse(block.overlaps(new TimeBlock(9, 0, 10, 0)));
        assertFalse(block.overlaps(new TimeBlock(4, 0, 4, 52)));
        assertFalse(block.overlaps(new TimeBlock(8, 24, 11, 0)));
    }

    @Test
    void equals() {
        TimeBlock block = new TimeBlock(4, 53, 8, 23);
        assertEquals(block, new TimeBlock(4, 53, 8, 23));
        assertNotEquals(block, new TimeBlock(4, 53, 8, 24));
    }

    @Test
    void compareTo() {
        TimeBlock block = new TimeBlock(4, 53, 8, 23);
        assertEquals(1, block.compareTo(new TimeBlock(4, 52, 8, 23)));
        assertEquals(0, block.compareTo(new TimeBlock(4, 53, 8, 23)));
        assertEquals(-1, block.compareTo(new TimeBlock(4, 54, 8, 23)));

        assertEquals(-1,new TimeBlock(4, 52, 8, 23).compareTo(block));
        assertEquals(0, new TimeBlock(4, 53, 8, 23).compareTo(block));
        assertEquals(1, new TimeBlock(4, 54, 8, 23).compareTo(block));
    }

    @Test
    void extend() {
        assertEquals(new TimeBlock(4, 23, 16, 35),
                new TimeBlock(4, 23, 10, 11).extend(
                        new TimeBlock(9, 44, 16, 35)
                ));
    }
}