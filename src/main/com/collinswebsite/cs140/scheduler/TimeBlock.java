package com.collinswebsite.cs140.scheduler;

/**
 * Represents a block of time
 */
public class TimeBlock implements Comparable<TimeBlock> {
    private final int beginHour;
    private final int endHour;
    private final int beginMinute;
    private final int endMinute;

    public TimeBlock(int beginHour, int beginMinute, int endHour, int endMinute) {
        this.beginHour = beginHour;
        this.endHour = endHour;
        this.beginMinute = beginMinute;
        this.endMinute = endMinute;
    }

    /**
     * @return Formats the time block as 12:34A-04:53P
     */
    public String toString() {
        return String.format("%02d:%02d%s-%02d:%02d%s",
                Math.floorMod(beginHour - 1, 12) + 1, // floorMod used here to fix edge case for midnight (-1 % 12 = -1, not 11)
                beginMinute,
                beginHour >= 12 ? "P" : "A",
                Math.floorMod(endHour - 1, 12) + 1,
                endMinute,
                endHour >= 12 ? "P" : "A");
    }

    /**
     * @param hour Hour to compare against (0-23)
     * @param minute Minute to compare against (0-60)
     * @return True if this time block begins before or at the given time.
     */
    public boolean beginsBefore(int hour, int minute) {
        return hour > beginHour || (hour == beginHour && minute >= beginMinute);
    }

    /**
     * @param hour Hour to compare against (0-23)
     * @param minute Minute to compare against (0-60)
     * @return True if this time block ends after or at the given time.
     */
    public boolean endsAfter(int hour, int minute) {
        return hour < endHour || (hour == endHour && minute <= endMinute);
    }

    /**
     * @param hour Hour to compare against (0-23)
     * @param minute Minute to compare against (0-60)
     * @return True if this time block contains the given time, including boundaries.
     */
    public boolean contains(int hour, int minute) {
        return beginsBefore(hour, minute) && endsAfter(hour, minute);
    }

    /**
     * @param other Time block to compare against
     * @return True if this time block overlaps at all with the given time block.
     */
    public boolean overlaps(TimeBlock other) {
        return contains(other.beginHour, other.beginMinute) || contains(other.endHour, other.endMinute) ||
                other.contains(beginHour, beginMinute) || other.contains(endHour, endMinute);
    }

    @Override
    public boolean equals(Object otherObject) {
        if(!(otherObject instanceof TimeBlock)) {
            return false;
        }
        TimeBlock other = (TimeBlock) otherObject;
        return beginHour == other.beginHour && beginMinute == other.beginMinute &&
                endHour == other.endHour && endMinute == other.endMinute;
    }

    @Override
    public int compareTo(TimeBlock o) {
        if(beginHour == o.beginHour && beginMinute == o.beginMinute) {
            return 0;
        } else if(this.beginsBefore(o.beginHour, o.beginMinute)) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * @param other Another time block.
     * @return A new time block that encompasses both this time block and the other one.
     */
    public TimeBlock extend(TimeBlock other) {
        TimeBlock beginsFirst = this.beginsBefore(other.beginHour, other.beginMinute) ? this : other;
        TimeBlock endsLast = this.endsAfter(other.endHour, other.endMinute) ? this : other;
        return new TimeBlock(beginsFirst.beginHour, beginsFirst.beginMinute, endsLast.endHour, endsLast.endMinute);
    }
}
