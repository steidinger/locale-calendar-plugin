package org.acm.steidinger.calendar;

import java.util.Date;

public class CalendarEntry {
    public Date begin;
    public Date end;
    public String title;

    CalendarEntry(Long begin, Long end, String title) {
        this.begin = new Date(begin);
        this.end = new Date(end);
        this.title = title;
    }

    @Override
    public String toString() {
        return (begin == null ? "n/a" : begin.toString()) + " - " + (end == null ? "n/a" : end.toString()) + ": " + title;
    }
}
