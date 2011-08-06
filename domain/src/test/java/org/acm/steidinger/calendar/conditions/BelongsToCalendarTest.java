package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.Condition;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BelongsToCalendarTest {
    private static final String CALENDAR_ONE = "1";
    private static final String CALENDAR_TWO = "2";
    private Condition condition;


    private CalendarEntry entryWithId(String id) {
        return new CalendarEntry(id, System.currentTimeMillis(),  System.currentTimeMillis(), "title", 0);
    }

    @Test
    public void matches_shouldReturnFalseForEntryWithDifferentCalendarID() {
        condition = new BelongsToCalendar(CALENDAR_ONE);

        boolean match = condition.matches(entryWithId(CALENDAR_TWO));

        assertThat(match, is(false));
    }

    @Test
    public void matches_shouldReturnTrueForEntryWithCalendarIdInExpectedIds() {
        condition = new BelongsToCalendar(newArrayList(CALENDAR_ONE, CALENDAR_TWO));

        boolean match = condition.matches(entryWithId(CALENDAR_TWO));

        assertThat(match, is(true));
    }

    @Test
    public void matches_shouldReturnTrueForEntryWithExpectedSingleCalendarID() {
        condition = new BelongsToCalendar(CALENDAR_ONE);

        boolean match = condition.matches(entryWithId(CALENDAR_ONE));

        assertThat(match, is(true));
    }
}
