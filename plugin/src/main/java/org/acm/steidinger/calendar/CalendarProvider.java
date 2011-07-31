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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarProvider {
    private static Uri providerUri(String path) {
        String base;
        if (Integer.parseInt(Build.VERSION.SDK) < 8) {
            base = "content://calendar";
        }
        else {
            base = "content://com.android.calendar";
        }
        return Uri.parse(base + path);
    }

    public static List<CalendarInfo> getCalendars(Context context) {
        List<CalendarInfo> calendars = new ArrayList<CalendarInfo>();
        final Cursor cursor = context.getContentResolver().query(providerUri("/calendars"),
                (new String[]{"_id", "displayName", "selected"}), null, null, null);
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    CalendarInfo calendarInfo = new CalendarInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                    if (calendarInfo.selected) {
                        calendars.add(calendarInfo);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return calendars;
    }

    public static List<CalendarEntry> getNextCalendarEntries(Context context, String calendarID) {

        ContentResolver contentResolver = context.getContentResolver();
        List<CalendarEntry> entries = new ArrayList<CalendarEntry>();

        Uri.Builder builder = providerUri("/instances/when").buildUpon();
        long now = new Date().getTime();
        ContentUris.appendId(builder, now);
        ContentUris.appendId(builder, now + DateUtils.DAY_IN_MILLIS);

        Cursor eventCursor = contentResolver.query(builder.build(),
                new String[]{"title", "begin", "end", "allDay"}, "Calendars._id=" + calendarID,
                null, "startDay ASC, startMinute ASC");
        // For a full list of available columns see http://tinyurl.com/yfbg76w

        if (eventCursor != null) {
            try {
                while (eventCursor.moveToNext()) {
                    entries.add(new CalendarEntry(eventCursor.getLong(1), eventCursor.getLong(2), eventCursor.getString(0), eventCursor.getInt(3)));
                }
            } finally {
                eventCursor.close();
            }
        }
        return entries;
    }

}
