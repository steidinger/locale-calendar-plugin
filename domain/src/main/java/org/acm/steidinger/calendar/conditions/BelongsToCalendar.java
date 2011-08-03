package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.Condition;

public class BelongsToCalendar extends Condition {
    private final String[] ids;

    public BelongsToCalendar(String[] ids) {
        this.ids = ids;
    }

    @Override
    public boolean matches(CalendarEntry entry) {
        for (String id: ids) {
            if (entry.calendarID.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
