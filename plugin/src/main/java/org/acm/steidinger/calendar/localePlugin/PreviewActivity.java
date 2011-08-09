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

package org.acm.steidinger.calendar.localePlugin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import org.acm.steidinger.calendar.CalendarProvider;
import org.acm.steidinger.calendar.ConditionGroup;

public class PreviewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_preview);
        ConditionGroup conditions = (ConditionGroup) this.getIntent().getExtras().get("conditions");
        String id = conditions.getCalendarIds().size() < 1 ? null : conditions.getCalendarIds().get(0);
        ((TextView) this.findViewById(R.id.preview_condition)).setText(conditions.toString());
        ((ListView) this.findViewById(R.id.preview_entries)).setAdapter(new CalendarAdapter(this,
                CalendarProvider.getNextCalendarEntries(this, conditions.getCalendarIds(), 5), conditions));
    }
}
