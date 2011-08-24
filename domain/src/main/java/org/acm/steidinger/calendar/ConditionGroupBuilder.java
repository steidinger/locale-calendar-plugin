package org.acm.steidinger.calendar;

import org.acm.steidinger.calendar.conditions.*;

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

    public ConditionGroupBuilder titleNotContainingWords(String words) {
        if (words != null && words.trim().length() > 0) {
            String[] split = words.split("(,|\\s)+");
            for (String word : split) {
                group.conditions.add(new TitleDoesNotContainText(word));
            }
        }
        return this;
    }

    public ConditionGroupBuilder titleContainingWords(String words) {
        if (words != null && words.trim().length() > 0) {
            String[] split = words.split("(,|\\s)+");
            for (String word : split) {
                group.conditions.add(new TitleDoesContainText(word));
            }
        }
        return this;
    }
    public ConditionGroupBuilder locationNotContainingWords(String words) {
        if (words != null && words.trim().length() > 0) {
            String[] split = words.split("(,|\\s)+");
            for (String word : split) {
                group.conditions.add(new LocationDoesNotContainText(word));
            }
        }
        return this;
    }

    public ConditionGroupBuilder locationContainingWords(String words) {
        if (words != null && words.trim().length() > 0) {
            String[] split = words.split("(,|\\s)+");
            for (String word : split) {
                group.conditions.add(new LocationDoesContainText(word));
            }
        }
        return this;
    }

    public ConditionGroup build() {
        return group;
    }
}
