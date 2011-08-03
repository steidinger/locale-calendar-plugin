package org.acm.steidinger.calendar;

import java.util.ArrayList;
import java.util.List;

public class ConditionGroup {
    protected final List<Condition> conditions = new ArrayList<Condition>();

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
}
