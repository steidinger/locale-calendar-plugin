package org.acm.steidinger.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarProvider {
    public static List<CalendarInfo> getCalendars(Context context) {
        List<CalendarInfo> calendars = new ArrayList<CalendarInfo>();
        final Cursor cursor = context.getContentResolver().query(Uri.parse("content://calendar/calendars"),
                (new String[]{"_id", "displayName", "selected"}), null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                CalendarInfo calendarInfo = new CalendarInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                if (calendarInfo.selected) {
                    calendars.add(calendarInfo);
                }
            }
        }
        return calendars;
    }

    public static List<CalendarEntry> getNextCalendarEntries(Context context, String calendarID) {

        ContentResolver contentResolver = context.getContentResolver();
        List<CalendarEntry> entries = new ArrayList<CalendarEntry>();

        Uri.Builder builder = Uri.parse("content://calendar/instances/when").buildUpon();
        long now = new Date().getTime();
        ContentUris.appendId(builder, now);
        ContentUris.appendId(builder, now + DateUtils.DAY_IN_MILLIS);

        Cursor eventCursor = contentResolver.query(builder.build(),
                new String[]{"title", "begin", "end", "allDay"}, "Calendars._id=" + calendarID,
                null, "startDay ASC, startMinute ASC");
        // For a full list of available columns see http://tinyurl.com/yfbg76w

        while (eventCursor.moveToNext()) {
            entries.add(new CalendarEntry(eventCursor.getLong(1), eventCursor.getLong(2), eventCursor.getString(0)));
        }
        return entries;
    }

}
