/*
 * // Copyright 2011 Frank Steidinger,
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at <http://www.apache.org/licenses/LICENSE-2.0>
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 */
package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.Condition;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DoesContainTextTest {

    @Test
    public void matchesShouldReturnTrueForEntryWithExactTitleMatch() {
        String text = "test";
        CalendarEntry entry = entry(text);
        Condition condition = new DoesContainText(text);

        assertThat(condition.matches(entry), is(true));
    }

    @Test
    public void matchesShouldReturnTrueForEntryWithSubstringTitleMatch() {
        String text = "test";
        CalendarEntry entry = entry(text + " more text");
        Condition condition = new DoesContainText(text);

        assertThat(condition.matches(entry), is(true));
    }

    @Test
    public void matchesShouldReturnTrueIfTitleContainsTextInDifferentCase() {
        String text = "TEST";
        CalendarEntry entry = entry("test more text");
        Condition condition = new DoesContainText(text);

        assertThat(condition.matches(entry), is(true));
    }

    @Test
    public void matchesShouldReturnFalseIfTitleDoesNotContainText() {
        String text = "test";
        CalendarEntry entry = entry("some other text");
        Condition condition = new DoesContainText(text);

        assertThat(condition.matches(entry), is(false));
    }

    private CalendarEntry entry(String text) {
        return new CalendarEntry("any", System.currentTimeMillis(), System.currentTimeMillis(), text, 0);
    }
}
