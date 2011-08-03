package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.Condition;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class TimeConditionTest {
    static CalendarEntry entry(long start, long end) {
        return new CalendarEntry("anyId", start, end, "title", 0);
    }

    static long now() {
        return System.currentTimeMillis();
    }

    static long minutes(int min) {
        return min * 60 * 1000L;
    }

    public static class WithoutLeadTime {
        Condition condition = new TimeCondition(0);

        @Test
        public void conditionShouldMatchEntryStartingNowAndEndingLater() {
            assertThat(condition.matches(entry(now(), now() + minutes(5))), is(true));
        }

        @Test
        public void conditionShouldMatchEntryStarting5MinutesAgoAndEndingLater() {
            assertThat(condition.matches(entry(now() - minutes(5), now() + minutes(5))), is(true));
        }

        @Test
        public void conditionShouldNotMatchEntryStartingIn5MinutesAndEndingLater() {
            assertThat(condition.matches(entry(now() + minutes(5), now() + minutes(15))), is(false));
        }

        @Test
        public void conditionShouldMatchEntryStarting5MinutesAgoAndEndingNow() {
            assertThat(condition.matches(entry(now() - minutes(5), now())), is(true));
        }

        @Test
        public void conditionShouldNotMatchEntryEndingBeforeNow() {
            assertThat(condition.matches(entry(now() - minutes(5), now() - minutes(1))), is(false));
        }
    }
    public static class WithLeadTime {
        Condition condition = new TimeCondition(15);

        @Test
        public void conditionShouldMatchEntryStartingNowAndEndingLater() {
            assertThat(condition.matches(entry(now(), now() + minutes(5))), is(true));
        }

        @Test
        public void conditionShouldMatchEntryStarting15MinutesAgoAndEndingLater() {
            assertThat(condition.matches(entry(now() - minutes(15), now() + minutes(5))), is(true));
        }

        @Test
        public void conditionShouldMatchEntryStarting20MinutesAgoAndEndingLater() {
            assertThat(condition.matches(entry(now() - minutes(20), now() + minutes(5))), is(true));
        }

        @Test
        public void conditionShouldNotMatchEntryStartingIn20MinutesAndEndingLater() {
            assertThat(condition.matches(entry(now() + minutes(20), now() + minutes(50))), is(false));
        }

        @Test
        public void conditionShouldMatchEntryStarting5MinutesAgoAndEndingNow() {
            assertThat(condition.matches(entry(now() - minutes(5), now())), is(true));
        }

        @Test
        public void conditionShouldNotMatchEntryEndingBeforeNow() {
            assertThat(condition.matches(entry(now() - minutes(5), now() - minutes(1))), is(false));
        }
    }
}
