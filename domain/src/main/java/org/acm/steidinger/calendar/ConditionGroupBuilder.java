package org.acm.steidinger.calendar;

import org.acm.steidinger.calendar.conditions.BelongsToCalendar;
import org.acm.steidinger.calendar.conditions.DoesNotContainText;
import org.acm.steidinger.calendar.conditions.NotAllDayEvent;
import org.acm.steidinger.calendar.conditions.TimeCondition;

public class ConditionGroupBuilder {
    private ConditionGroup group;

    private ConditionGroupBuilder() {
    }

    public static ConditionGroupBuilder all() {
        return new ConditionGroupBuilder();
    }

    public ConditionGroupBuilder withCalendarId(String id) {
        group.conditions.add(0, new BelongsToCalendar(new String[] { id }));
        return this;
    }

    public ConditionGroupBuilder withLeadTimeInMinutes(int minutes) {
        group.conditions.add(new TimeCondition(minutes));
        return this;
    }

    public ConditionGroupBuilder ignoringAllDayEvents(boolean ignore) {
        if (ignore) {
            group.conditions.add(new NotAllDayEvent());
        }
        return this;
    }

    public ConditionGroupBuilder notContainingWords(String words) {
        if (words != null && words.trim().length() > 0) {
            String[] split = words.split("(,|\\s)+");
            for (String word : split) {
                group.conditions.add(new DoesNotContainText(word));
            }
        }
        return this;
    }

    public ConditionGroup build() {
        return group;
    }
}
