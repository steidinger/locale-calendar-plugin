package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.Condition;

public class NotAllDayEvent extends Condition {
    @Override
    public boolean matches(CalendarEntry entry) {
        return !entry.allDay;
    }

    public String toString() {
        return "ignoringAllDayEvents";
    }
}
