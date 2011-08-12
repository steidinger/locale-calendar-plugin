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
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import org.acm.steidinger.calendar.localePlugin.Constants;

import java.util.ArrayList;
import java.util.Arrays;
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
        if (Constants.IS_DEBUG) {
            return getDummyCalendars();
        }
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

    public static List<CalendarEntry> getNextCalendarEntries(Context context, ArrayList<String> calendarIds) {
        return getNextCalendarEntries(context, calendarIds, 1);
    }

    public static List<CalendarEntry> getNextCalendarEntries(Context context, ArrayList<String> calendarIds, int days) {
        if (Constants.IS_DEBUG) {
            return getDummyEntries();
        }
        ContentResolver contentResolver = context.getContentResolver();
        List<CalendarEntry> entries = new ArrayList<CalendarEntry>();

        Uri.Builder builder = providerUri("/instances/when").buildUpon();
        long now = new Date().getTime();
        ContentUris.appendId(builder, now);
        ContentUris.appendId(builder, now + (days * DateUtils.DAY_IN_MILLIS));
        String selection;
        if (calendarIds.isEmpty()) {
            selection = "calendar_id is null"; // will always be false
        }
        else if (calendarIds.size() == 1) {
            selection = "calendar_id = " + calendarIds.get(0);
        }
        else {
            selection = "calendar_id in (" + TextUtils.join(",", calendarIds) + ")";
        }
        Cursor eventCursor = contentResolver.query(builder.build(),
                new String[]{"title", "begin", "end", "allDay", "calendar_id", "eventLocation", "description", "transparency"},
                selection,
                null, "startDay ASC, startMinute ASC");
        // For a full list of available columns see http://tinyurl.com/yfbg76w

        if (eventCursor != null) {
            try {
                while (eventCursor.moveToNext()) {

                    CalendarEntry entry = new CalendarEntry(eventCursor.getString(eventCursor.getColumnIndex("calendar_id")),
                            eventCursor.getLong(eventCursor.getColumnIndex("begin")),
                            eventCursor.getLong(eventCursor.getColumnIndex("end")),
                            eventCursor.getString(eventCursor.getColumnIndex("title")),
                            eventCursor.getString(eventCursor.getColumnIndex("description")),
                            eventCursor.getString(eventCursor.getColumnIndex("eventLocation")),
                            eventCursor.getInt(eventCursor.getColumnIndex("allDay")),
                            eventCursor.getInt(eventCursor.getColumnIndex("transparency")));
                    if (calendarIds.contains(entry.calendarID)) {
                        entries.add(entry);
                    }
                }
            } finally {
                eventCursor.close();
            }
        }
        return entries;
    }

    private static List<CalendarInfo> getDummyCalendars() {
        return Arrays.asList(new CalendarInfo("dummy1", "Test Calendar", "1"),
                new CalendarInfo("dummy2", "Second Test Calendar", "1"));
    }

    private static List<CalendarEntry> getDummyEntries() {
        final long now = System.currentTimeMillis();
        final long hourInMillis = 3600 * 1000;
        return Arrays.asList(new CalendarEntry("dummy1", now, now + hourInMillis, "Test Entry", "Description", "Somewhere", 0, CalendarEntry.STATUS_BUSY),
                new CalendarEntry("dummy1", now + 2 * hourInMillis, now + 3 * hourInMillis, "Test Entry 2", null, "Location", 0, CalendarEntry.STATUS_FREE),
                new CalendarEntry("dummy1", today(), today(), "Bank Holiday", null, null, 1, CalendarEntry.STATUS_FREE)
        );
    }

    private static long today() {
        Time time = new Time();
        time.setToNow();
        time.set(0, 0, 0, time.monthDay, time.month, time.year);
        return time.toMillis(false);
    }
}
