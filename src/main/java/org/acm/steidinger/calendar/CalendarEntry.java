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
    public Date begin;
    public Date end;
    public String title;

    CalendarEntry(Long begin, Long end, String title) {
        this.begin = new Date(begin);
        this.end = new Date(end);
        this.title = title;
    }

    @Override
    public String toString() {
        return (begin == null ? "n/a" : begin.toString()) + " - " + (end == null ? "n/a" : end.toString()) + ": " + title;
    }
}
