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

package org.acm.steidinger.calendar;

import org.acm.steidinger.calendar.conditions.TimeCondition;
import org.acm.steidinger.calendar.conditions.TitleDoesContainText;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ConditionGroupTest {
    @Test
    public void groupShouldMatchIfAllConditionsMatch() {
        ConditionGroup group = new ConditionGroup();
        group.conditions.add(new TitleDoesContainText("test"));
        group.conditions.add(new TitleDoesContainText("other"));

        assertThat(group.matches(entry("other test")), equalTo(true));
    }

    @Test
    public void groupShouldNotMatchIfOneConditionDoesNotMatch() {
        ConditionGroup group = new ConditionGroup();
        group.conditions.add(new TitleDoesContainText("test"));
        group.conditions.add(new TitleDoesContainText("other"));

        assertThat(group.matches(entry("just a test")), equalTo(false));
    }

    @Test
    public void leadTimeWithoutTimeConditionShouldBeOneDay() {
        ConditionGroup group = new ConditionGroup();
        
        assertThat(group.getLeadTimeInDays(), equalTo(1));
    }
    
    @Test
    public void leadTimeInDaysWithTimeConditionHavingLeadTime15MinutesShouldBeOneDay() {
        ConditionGroup group = new ConditionGroup();
        group.conditions.add(new TimeCondition(15));

        assertThat(group.getLeadTimeInDays(), equalTo(1));
    }
    
    @Test
    public void leadTimeInDaysWithTimeConditionHavingLeadTime1440MinutesShouldBeTwoDays() {
        ConditionGroup group = new ConditionGroup();
        int dayInMinutes = 1440;
        group.conditions.add(new TimeCondition(dayInMinutes));

        assertThat(group.getLeadTimeInDays(), equalTo(2));
    }

    @Test
    public void leadTimeInDaysWithTimeConditionHavingLeadTimeGreater1440MinutesShouldBeTwoDays() {
        ConditionGroup group = new ConditionGroup();
        int dayInMinutes = 1440;
        group.conditions.add(new TimeCondition(dayInMinutes + 1));

        assertThat(group.getLeadTimeInDays(), equalTo(2));
    }

    private CalendarEntry entry(String text) {
        return new CalendarEntry("any", System.currentTimeMillis(), System.currentTimeMillis(), text, "description",
                "location", 0, CalendarEntry.STATUS_BUSY);
    }
}
