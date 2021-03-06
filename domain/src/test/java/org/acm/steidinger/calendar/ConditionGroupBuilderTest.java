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

import org.acm.steidinger.calendar.conditions.*;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class ConditionGroupBuilderTest {
    @Test
    public void builderNotContainingWords_withNull_shouldNotAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().titleNotContainingWords(null).build();

        assertThat(group, not(hasConditionOfType(TitleDoesNotContainText.class)));
    }

    @Test
    public void builderNotContainingWords_withEmptyString_shouldNotAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().titleNotContainingWords("").build();

        assertThat(group, not(hasConditionOfType(TitleDoesNotContainText.class)));
    }

    @Test
    public void builderNotContainingWords_withSeparatorOnly_shouldNotAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().titleNotContainingWords(",").build();

        assertThat(group, not(hasConditionOfType(TitleDoesNotContainText.class)));
    }

    @Test
    public void builderNotContainingWords_withSeparatorAndSpacesOnly_shouldNotAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().titleNotContainingWords(" ,  ").build();

        assertThat(group, not(hasConditionOfType(TitleDoesNotContainText.class)));
    }

    @Test
    public void builderNotContainingWords_withSingleWord_shouldAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().titleNotContainingWords("test").build();

        assertThat(group, hasConditionOfType(TitleDoesNotContainText.class));
    }

    @Test
    public void builderNotContainingWords_withTwoWords_shouldAddTwoConditions() {
        ConditionGroup group = ConditionGroupBuilder.all().titleNotContainingWords("test, another").build();

        List<Condition> textConditions = newArrayList(filter(group.conditions, instanceOf(TitleDoesNotContainText.class)));
        assertThat(textConditions, hasItems(withText("test"), withText("another")));
    }

    @Test
    public void builderContainingWords_withNull_shouldNotAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().titleContainingWords(null).build();

        assertThat(group, not(hasConditionOfType(TitleDoesContainText.class)));
    }

    @Test
    public void builderContainingWords_withEmptyString_shouldNotAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().titleContainingWords("").build();

        assertThat(group, not(hasConditionOfType(TitleDoesContainText.class)));
    }

    @Test
    public void builderContainingWords_withSeparatorOnly_shouldNotAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().titleContainingWords(",").build();

        assertThat(group, not(hasConditionOfType(TitleDoesContainText.class)));
    }

    @Test
    public void builderContainingWords_withSeparatorAndSpacesOnly_shouldNotAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().titleContainingWords(" ,  ").build();

        assertThat(group, not(hasConditionOfType(TitleDoesContainText.class)));
    }

    @Test
    public void builderContainingWords_withSingleWord_shouldAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().titleContainingWords("test").build();

        assertThat(group, hasConditionOfType(TitleDoesContainText.class));
    }

    @Test
    public void builderContainingWords_withTwoWords_shouldAddDisjunctiveTwoConditions() {
        ConditionGroup group = ConditionGroupBuilder.all().titleContainingWords("test, another").build();

        List<Condition> textConditions = newArrayList(filter(((DisjunctiveConditionGroup) find(group.conditions,
                instanceOf(DisjunctiveConditionGroup.class))).conditions, instanceOf(TitleDoesContainText.class)));
        assertThat(textConditions, hasItems(withText("test"), withText("another")));
    }

    @Test
    public void builderContainingWords_withTwoWords_shouldNotAddConjunctiveTwoConditions() {
        ConditionGroup group = ConditionGroupBuilder.all().titleContainingWords("test, another").build();

        List<Condition> textConditions = newArrayList(filter(group.conditions, instanceOf(TitleDoesContainText.class)));
        assertThat(textConditions, not(hasItems(withText("test"), withText("another"))));
    }

    @Test
    public void builderForLocationContainingWords_withSingleWord_shouldAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().locationContainingWords("test").build();

        assertThat(group, hasConditionOfType(LocationDoesContainText.class));
    }

    @Test
    public void builderForLocationContainingWords_withTwoWords_shouldAddDisjunctiveTwoConditions() {
        ConditionGroup group = ConditionGroupBuilder.all().locationContainingWords("test, another").build();

        List<Condition> textConditions = newArrayList(filter(((DisjunctiveConditionGroup) find(group.conditions,
                instanceOf(DisjunctiveConditionGroup.class))).conditions, instanceOf(LocationDoesContainText.class)));
        assertThat(textConditions, hasItems(withLocationText("test"), withLocationText("another")));
    }

    @Test
    public void builderForLocationContainingWords_withTwoWords_shouldNotAddConjunctiveTwoConditions() {
        ConditionGroup group = ConditionGroupBuilder.all().locationContainingWords("test, another").build();

        List<Condition> textConditions = newArrayList(filter(group.conditions, instanceOf(LocationDoesContainText.class)));
        assertThat(textConditions, not(hasItems(withLocationText("test"), withLocationText("another"))));
    }

    @Test
    public void builderWithCalendarIdNull_shouldAddConditionWithNoCalendarId() {
        ConditionGroup group = ConditionGroupBuilder.all().withCalendarId(null).build();

        assertThat(group.conditions.get(0), allOf(CoreMatchers.instanceOf(BelongsToCalendar.class),
                hasCalendarIds(BelongsToCalendar.NO_CALENDAR)));
    }

    @Test
    public void builderWithCalendarIdEmpty_shouldNotAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().withCalendarId("").build();

        assertThat(group.conditions.get(0), allOf(CoreMatchers.instanceOf(BelongsToCalendar.class),
                hasCalendarIds(BelongsToCalendar.NO_CALENDAR)));
    }

    @Test
    public void builderWithCalendarIdNotEmpty_shouldAddCondition() {
        ConditionGroup group = ConditionGroupBuilder.all().withCalendarId("any").build();

        assertThat(group, hasConditionOfType(BelongsToCalendar.class));
    }


    public static TypeSafeMatcher<Condition> withText(final String expectedText) {
        return new TypeSafeMatcher<Condition>() {
            @Override
            public boolean matchesSafely(Condition condition) {
                return (condition instanceof TitleDoesNotContainText && ((TitleDoesNotContainText) condition).text.equals(expectedText))
                || (condition instanceof TitleDoesContainText && ((TitleDoesContainText) condition).text.equals(expectedText));
            }

            public void describeTo(Description description) {
                description.appendText("TitleDoes[Not]ContainText condition with text '" + expectedText + "'");
            }
        };
    }
    public static TypeSafeMatcher<Condition> withLocationText(final String expectedText) {
        return new TypeSafeMatcher<Condition>() {
            @Override
            public boolean matchesSafely(Condition condition) {
                return (condition instanceof LocationDoesNotContainText && ((LocationDoesNotContainText) condition).text.equals(expectedText))
                || (condition instanceof LocationDoesContainText && ((LocationDoesContainText) condition).text.equals(expectedText));
            }

            public void describeTo(Description description) {
                description.appendText("LocationDoes[Not]ContainText condition with text '" + expectedText + "'");
            }
        };
    }
    public static TypeSafeMatcher<ConditionGroup> hasConditionOfType(final Class<? extends Condition> conditionClass) {
        return new TypeSafeMatcher<ConditionGroup>() {
            @Override
            public boolean matchesSafely(ConditionGroup group) {
                if (group == null) {
                    return false;
                }
                for (Condition condition : group.conditions) {
                    if (conditionClass.isAssignableFrom(condition.getClass())) {
                        return true;
                    }
                }
                return false;
            }

            public void describeTo(Description description) {
                description.appendText("a ConditionGroup with a condition of type " + conditionClass.getSimpleName());
            }
        };
    }

    public static TypeSafeMatcher<Condition> hasCalendarIds(final ArrayList<String> expectedIds) {
        return new TypeSafeMatcher<Condition>() {
            @Override
            public boolean matchesSafely(Condition condition) {
                return condition instanceof BelongsToCalendar && expectedIds.equals(((BelongsToCalendar) condition).ids);
            }

            public void describeTo(Description description) {
                description.appendText("BelongsToCalendar with ids " + expectedIds);
            }
        };
    }
}
