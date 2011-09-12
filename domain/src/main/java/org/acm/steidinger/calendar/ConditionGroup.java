package org.acm.steidinger.calendar;

import org.acm.steidinger.calendar.conditions.BelongsToCalendar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConditionGroup implements Condition {
    protected final List<Condition> conditions = new ArrayList<Condition>();

    public boolean anyMatch(Collection<CalendarEntry> allEntries) {
        for (CalendarEntry entry : allEntries) {
            if (this.matches(entry)) {
                return true;
            }
        }
        return false;
    }

    public boolean matches(CalendarEntry entry) {
        for (Condition condition : conditions) {
            if (!condition.matches(entry)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return conditions.toString();
    }

    public ArrayList<String> getCalendarIds() {
        for (Condition condition : conditions) {
            if (condition instanceof BelongsToCalendar) {
                return ((BelongsToCalendar) condition).ids;
            }
        }
        return new ArrayList<String>();
    }
}
