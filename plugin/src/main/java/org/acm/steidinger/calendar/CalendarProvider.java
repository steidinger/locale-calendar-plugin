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

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import org.acm.steidinger.calendar.localePlugin.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CalendarProvider {

    public static List<CalendarInfo> getCalendars(Context context) {
        if (Constants.IS_DEBUG) {
            return getDummyCalendars();
        }
        List<CalendarInfo> calendars = new ArrayList<CalendarInfo>();
        final Cursor cursor = context.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI,
                (new String[]{CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                        CalendarContract.Calendars.VISIBLE}), null, null, null);
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    CalendarInfo calendarInfo = new CalendarInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                    calendars.add(calendarInfo);
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
        debug("Querying calendar entries for calendars: " + calendarIds.toString());
        List<CalendarEntry> entries = new ArrayList<CalendarEntry>();
        String selection;
/*
        if (calendarIds.isEmpty()) {
            selection = CalendarContract.Instances.CALENDAR_ID + " is null"; // will always be false
        }
        else if (calendarIds.size() == 1) {
            selection = CalendarContract.Instances.CALENDAR_ID + " = " + calendarIds.get(0);
        }
        else {
            selection = CalendarContract.Instances.CALENDAR_ID + " in (" + TextUtils.join(",", calendarIds) + ")";
        }
*/
        long now = new Date().getTime();
        Cursor eventCursor = CalendarContract.Instances.query(context.getContentResolver(),
                new String[]{CalendarContract.Instances.TITLE,
                        CalendarContract.Instances.DTSTART,
                        CalendarContract.Instances.DTEND,
                        CalendarContract.Instances.BEGIN,
                        CalendarContract.Instances.END,
                        CalendarContract.Instances.DURATION,
                        CalendarContract.Instances.ALL_DAY,
                        CalendarContract.Instances.CALENDAR_ID,
                        CalendarContract.Instances.EVENT_LOCATION,
                        CalendarContract.Instances.DESCRIPTION,
                        CalendarContract.Instances.AVAILABILITY},
                now,
                now + (days * DateUtils.DAY_IN_MILLIS));
//                selection);
        if (eventCursor != null) {
            try {
                while (eventCursor.moveToNext()) {
//                    dump(eventCursor);
                    CalendarEntry entry = new CalendarEntry(eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Instances.CALENDAR_ID)),
                            eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Instances.BEGIN)),
                            eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Instances.END)),
                            eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Instances.TITLE)),
                            eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Instances.DESCRIPTION)),
                            eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Instances.EVENT_LOCATION)),
                            eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Instances.ALL_DAY)),
                            eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Instances.AVAILABILITY)));
                    Log.d(Constants.LOG_TAG, "got calendar entry " + entry);
                    if (calendarIds.contains(entry.calendarID)) {
                        debug("Entry belongs to selected calendars");
                        entries.add(entry);
                    }
                    else {
                        debug("Entry ignored, does not belong to selected calendars");
                    }
                }
            } finally {
                eventCursor.close();
            }
        }
        return entries;
    }

    private static void dump(Cursor cursor) {
        StringBuilder s = new StringBuilder();
        String columnName;
        String value;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            columnName = cursor.getColumnName(i);
            value = cursor.getString(i);
            if (columnName.equals(CalendarContract.Instances.DTSTART) 
                    || columnName.equals(CalendarContract.Instances.DTEND) 
                    || columnName.equals(CalendarContract.Instances.BEGIN) 
                    || columnName.equals(CalendarContract.Instances.END)) {
                if (value != null) {
                    value = format.format(new Date(cursor.getLong(i)));
                }
            }
            s.append(columnName).append("=").append(value).append("\n");
        }
        Log.d(Constants.LOG_TAG, s.toString());
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

    private static void debug(final String msg) {
        if (Constants.IS_LOGGABLE) {
            Log.d(Constants.LOG_TAG, msg);
        }
    }

}
