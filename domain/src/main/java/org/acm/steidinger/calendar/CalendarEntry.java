// Copyright 2011 Frank Steidinger,
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at <http://www.apache.org/licenses/LICENSE-2.0>
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.acm.steidinger.calendar;

import java.util.Date;

public class CalendarEntry {
    public static final int STATUS_TENTATIVE = 2;
    public static final int STATUS_FREE = 1;
    public static final int STATUS_BUSY = 0;

    public final String calendarID;
    public final Date begin;
    public final Date end;
    public final String title;
    public final String description;
    public final String location;
    public final boolean allDay;
    public final int status;

    public CalendarEntry(String calendarID, long begin, long end, String title, String description,
                         String location, int allDay, int eventStatus) {
        this.calendarID = calendarID;
        this.begin = new Date(begin);
        this.end = new Date(end);
        this.title = title;
        this.description = description;
        this.location = location;
        this.allDay = allDay == 1;
        this.status = eventStatus;
    }

    @Override
    public String toString() {
        return (begin == null ? "n/a" : begin.toString()) + " - " + (end == null ? "n/a" : end.toString()) + ": " + title;
    }
}
