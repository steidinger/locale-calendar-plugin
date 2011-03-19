package org.acm.steidinger.calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.acm.steidinger.calendar.localePlugin.Constants;
import org.acm.steidinger.calendar.localePlugin.R;

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
        List<CalendarInfo> calendars = CalendarProvider.getCalendars(this);
        ArrayAdapter<CalendarInfo> adapter = new ArrayAdapter<CalendarInfo>(this, android.R.layout.simple_spinner_item,
                calendars.toArray(new CalendarInfo[calendars.size()]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner calendarSpinner = (Spinner) findViewById(R.id.calendarSpinner);
        calendarSpinner.setAdapter(adapter);
        final Spinner spinner = (Spinner) findViewById(R.id.calendarStateSpinner);
        final ArrayAdapter<String> states= new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, new String[] {
                        getString(R.string.booked),
                        getString(R.string.free)});
        states.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(states);

    }

    @Override
    public void finish() {
        Spinner calendarSpinner = (Spinner) findViewById(R.id.calendarSpinner);
        if (calendarSpinner != null) {
            CalendarInfo info = (CalendarInfo) calendarSpinner.getSelectedItem();
            if (info == null) {
                Log.d(Constants.LOG_TAG, "No calendar selected");
            } else {
                Log.d(Constants.LOG_TAG, "Calendar selected: " + info);
            }
        }
        final Spinner spinner = (Spinner) findViewById(R.id.calendarStateSpinner);
        if (spinner != null) {
            boolean booked = getString(R.string.booked).equals(spinner.getSelectedItem());
            Log.d(Constants.LOG_TAG, "Calendar should be " + (booked ? "booked" : "free"));
        }
        super.finish();
    }

}
