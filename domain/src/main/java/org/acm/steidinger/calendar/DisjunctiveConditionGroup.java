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

/**
 * A group of conditions that matches if at least one of its child conditions matches an entry.
 */
public class DisjunctiveConditionGroup extends ConditionGroup {
    @Override
    public boolean matches(CalendarEntry entry) {
        for (Condition condition : this.conditions) {
            if (condition.matches(entry)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Either: " + super.toString();
    }
}
