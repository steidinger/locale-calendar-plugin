package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.CalendarEntry;

public class TitleDoesNotContainText extends TextCondition {

    public TitleDoesNotContainText(String text) {
        super(text, false);
    }

    public boolean matches(CalendarEntry entry) {
        return entry.title != null && super.matches(entry.title);
    }

    @Override
    public String toString() {
        return "title notContaining '" + text + '\'';
    }
}
