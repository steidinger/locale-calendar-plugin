package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.Condition;

public class DoesNotContainText extends Condition {
    public final String text;

    public DoesNotContainText(String text) {
        this.text = text.toLowerCase();
    }

    @Override
    public boolean matches(CalendarEntry entry) {
        return entry.title != null && !entry.title.toLowerCase().contains(text);
    }

    @Override
    public String toString() {
        return "notContaining '" + text + '\'';
    }
}
