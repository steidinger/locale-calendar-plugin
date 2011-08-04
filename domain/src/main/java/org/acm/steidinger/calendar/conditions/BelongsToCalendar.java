package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.Condition;

import java.util.Arrays;

public class BelongsToCalendar extends Condition {
    public static final String[] NO_CALENDAR = new String[] {};

    public final String[] ids;

    public BelongsToCalendar(final String[] ids) {
        if (ids == null || ids.length == 0) {
            this.ids = NO_CALENDAR;
        }
        else if(ids.length == 1 && (ids[0] == null || ids[0].trim().isEmpty())) {
            this.ids = NO_CALENDAR;
        }
        else {
            this.ids = ids;
        }
    }

    @Override
    public boolean matches(final CalendarEntry entry) {
        for (String id: ids) {
            if (entry.calendarID.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "calendar ID " + Arrays.toString(ids);
    }
}
