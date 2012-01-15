package org.acm.steidinger.calendar;

import org.acm.steidinger.calendar.conditions.BelongsToCalendar;
import org.acm.steidinger.calendar.conditions.TimeCondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConditionGroup implements Condition {
    protected final List<Condition> conditions = new ArrayList<Condition>();
    private static final long MILLIS_PER_DAY = 1000L * 60 * 60 * 24;
    
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
    
    public int getLeadTimeInDays() {
        long leadTimeInMillis = 0;
        for (Condition condition : conditions) {
            if (condition instanceof TimeCondition) {
                leadTimeInMillis = ((TimeCondition) condition).leadTimeInMillis;
            }
        }
        Double leadTimeInDays = Math.ceil((leadTimeInMillis + 1) * 1.0 / MILLIS_PER_DAY);
        return Math.max(1, leadTimeInDays.intValue());
    }
}
