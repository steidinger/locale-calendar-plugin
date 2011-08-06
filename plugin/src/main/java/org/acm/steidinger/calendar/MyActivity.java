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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import org.acm.steidinger.calendar.localePlugin.Constants;
import org.acm.steidinger.calendar.localePlugin.R;

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
        final Spinner spinner = (Spinner) findViewById(R.id.calendarStateSpinner);
        if (spinner != null) {
            boolean booked = getString(R.string.booked).equals(spinner.getSelectedItem());
            Log.d(Constants.LOG_TAG, "Calendar should be " + (booked ? "booked" : "free"));
        }
        super.finish();
    }

}
