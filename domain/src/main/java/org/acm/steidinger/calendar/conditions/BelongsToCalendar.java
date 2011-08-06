package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.Condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class BelongsToCalendar extends Condition {
    public static final ArrayList<String> NO_CALENDAR = new ArrayList<String>();

    public final ArrayList<String> ids;

    public BelongsToCalendar(final String id) {
        if (id == null || id.length() == 0) {
            this.ids = NO_CALENDAR;
        }
        else {
            this.ids = new ArrayList<String>(1);
            ids.add(id);
        }
    }

    public BelongsToCalendar(final ArrayList<String> ids) {
        if (ids == null || ids.isEmpty()) {
            this.ids = NO_CALENDAR;
        }
        else {
            Iterator<String> it = ids.iterator();
            while (it.hasNext()) {
                String id = it.next();
                if (id == null || id.length() == 0) {
                    it.remove();
                }
            }
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
        return "calendar ID " + ids.toString();
    }
}
