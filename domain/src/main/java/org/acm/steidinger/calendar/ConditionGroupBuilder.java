package org.acm.steidinger.calendar;

import org.acm.steidinger.calendar.conditions.BelongsToCalendar;
import org.acm.steidinger.calendar.conditions.DoesNotContainText;
import org.acm.steidinger.calendar.conditions.NotAllDayEvent;
import org.acm.steidinger.calendar.conditions.TimeCondition;

import java.util.ArrayList;

public class ConditionGroupBuilder {
    private ConditionGroup group;

    private ConditionGroupBuilder() {
        group = new ConditionGroup();
    }

    public static ConditionGroupBuilder all() {
        return new ConditionGroupBuilder();
    }

    public ConditionGroupBuilder withCalendarId(String id) {
        group.conditions.add(0, new BelongsToCalendar(id));
        return this;
    }

    public ConditionGroupBuilder withCalendarIds(ArrayList<String> ids) {
        group.conditions.add(0, new BelongsToCalendar(ids));
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
