package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.Condition;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DoesNotContainTextTest {

    @Test
    public void matchesShouldReturnFalseForEntryWithExactTitleMatch() {
        String text = "test";
        CalendarEntry entry = entry(text);
        Condition condition = new DoesNotContainText(text);

        assertThat(condition.matches(entry), is(false));
    }

    @Test
    public void matchesShouldReturnFalseForEntryWithSubstringTitleMatch() {
        String text = "test";
        CalendarEntry entry = entry(text + " more text");
        Condition condition = new DoesNotContainText(text);

        assertThat(condition.matches(entry), is(false));
    }

    @Test
    public void matchesShouldReturnFalseIfTitleContainsTextInDifferentCase() {
        String text = "TEST";
        CalendarEntry entry = entry("test more text");
        Condition condition = new DoesNotContainText(text);

        assertThat(condition.matches(entry), is(false));
    }

    @Test
    public void matchesShouldReturnTrueIfTitleDoesNotContainText() {
        String text = "test";
        CalendarEntry entry = entry("some other text");
        Condition condition = new DoesNotContainText(text);

        assertThat(condition.matches(entry), is(true));
    }

    private CalendarEntry entry(String text) {
        return new CalendarEntry("any", System.currentTimeMillis(), System.currentTimeMillis(), text, "description",
                "location", 0, CalendarEntry.STATUS_BUSY);
    }
}
