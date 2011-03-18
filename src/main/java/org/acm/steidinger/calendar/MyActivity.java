package org.acm.steidinger.calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView calendarListView = (ListView) findViewById(R.id.calendarList);
        List<CalendarInfo> calendars = getCalendars(this);
        String[] calendarStrings = new String[calendars.size()];
        for (int i = 0; i < calendars.size(); i++) {
            calendarStrings[i] = calendars.get(i).toString();
        }
        calendarListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, calendarStrings));
/*
        List<CalendarEntry> entries = getNextCalendarEntries(this);
        textView.append("\n\nUpcoming appointments:\n\n");
        for (CalendarEntry entry : entries) {
            textView.append(entry.toString());
            textView.append("\n");
        }
*/
    }

    public static List<CalendarInfo> getCalendars(Context context) {
        List<CalendarInfo> calendars = new ArrayList<CalendarInfo>();
        final Cursor cursor = context.getContentResolver().query(Uri.parse("content://calendar/calendars"),
                (new String[]{"_id", "displayName", "selected"}), null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                calendars.add(new CalendarInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }
        return calendars;
    }

    public static List<CalendarEntry> getNextCalendarEntries(Context context) {

        ContentResolver contentResolver = context.getContentResolver();
        List<CalendarEntry> entries = new ArrayList<CalendarEntry>();

        final Cursor cursor = contentResolver.query(Uri.parse("content://calendar/calendars"),
                (new String[]{"_id", "displayName", "selected"}), null, null, null);
        // For a full list of available columns see http://tinyurl.com/yfbg76w
        if (cursor == null) {
            return entries;
        }
        HashSet<String> calendarIds = new HashSet<String>();

        while (cursor.moveToNext()) {
            if (!cursor.getString(2).equals("0")) {
                calendarIds.add(cursor.getString(0));
            }
        }

        // For each calendar, display all the events from the previous week to the end of next week.
        for (String id : calendarIds) {
            Uri.Builder builder = Uri.parse("content://calendar/instances/when").buildUpon();
            long now = new Date().getTime();
            ContentUris.appendId(builder, now);
            ContentUris.appendId(builder, now + DateUtils.WEEK_IN_MILLIS);

            Cursor eventCursor = contentResolver.query(builder.build(),
                    new String[]{"title", "begin", "end", "allDay"}, "Calendars._id=" + id,
                    null, "startDay ASC, startMinute ASC");
            // For a full list of available columns see http://tinyurl.com/yfbg76w

            while (eventCursor.moveToNext()) {
                entries.add(new CalendarEntry(eventCursor.getLong(1), eventCursor.getLong(2), eventCursor.getString(0)));
            }
        }
        return entries;
    }

    private static class CalendarEntry {
        Date begin;
        Date end;
        String title;

        private CalendarEntry(Long begin, Long end, String title) {
            this.begin = new Date(begin);
            this.end = new Date(end);
            this.title = title;
        }

        @Override
        public String toString() {
            return (begin == null ? "n/a" : begin.toString()) + " - " + (end == null ? "n/a" : end.toString()) + ": " + title;
        }
    }
}
