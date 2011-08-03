package org.acm.steidinger.calendar;

import org.acm.steidinger.calendar.CalendarEntry;

public abstract class Condition {
    public abstract boolean matches(CalendarEntry entry);

}
