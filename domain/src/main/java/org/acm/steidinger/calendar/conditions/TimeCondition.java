package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.Condition;

public class TimeCondition implements Condition {
    private static final long MINUTE_IN_MILLIS = 60 * 1000;
    private final long leadTimeInMillis;

    public TimeCondition(int leadTimeInMinutes) {
        this.leadTimeInMillis = leadTimeInMinutes * MINUTE_IN_MILLIS;
    }

    public boolean matches(final CalendarEntry entry) {
        final long now = System.currentTimeMillis();
        return entry.begin.getTime() <= now + leadTimeInMillis && entry.end.getTime() >= now;
    }

    @Override
    public String toString() {
        return "leadTime=" + (leadTimeInMillis / MINUTE_IN_MILLIS);
    }
}
