package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.Condition;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NotAllDayEventTest {

    private Condition condition;

    @Before
    public void setUp() throws Exception {
        condition = new NotAllDayEvent();
    }

    @Test
    public void matchesShouldReturnFalseForAllDayEvent() {
        assertThat(condition.matches(entry(1)), is(false));

    }

    @Test
    public void matchesShouldReturnTrueForAllDayEvent() {
        assertThat(condition.matches(entry(0)), is(true));

    }

    private CalendarEntry entry(int allDay) {
        return new CalendarEntry("any", System.currentTimeMillis(), System.currentTimeMillis(), "title", "description",
                "location", allDay, CalendarEntry.STATUS_BUSY);
    }
}
