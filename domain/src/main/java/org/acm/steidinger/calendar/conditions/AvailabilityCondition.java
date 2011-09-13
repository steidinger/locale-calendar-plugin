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

public class AvailabilityCondition implements Condition {
    public final int expectedStatus;

    public AvailabilityCondition(int expectedStatus) {
        this.expectedStatus = expectedStatus;
    }

    public boolean matches(CalendarEntry entry) {
        return entry.status == expectedStatus;
    }

    @Override
    public String toString() {
        return "availability status: " + (expectedStatus == CalendarEntry.STATUS_BUSY ? "busy" : "available");
    }
}
