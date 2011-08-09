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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.ConditionGroup;
import org.apache.http.impl.cookie.DateUtils;

import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private final List<CalendarEntry> entries;
    private final Context context;
    private final ConditionGroup conditions;

    public CalendarAdapter(Context context, List<CalendarEntry> entries, ConditionGroup conditions) {
        this.entries = entries;
        this.context = context;
        this.conditions = conditions;
    }

    public int getCount() {
        return entries.size();
    }

    public Object getItem(int index) {
        return entries.get(index);
    }

    public long getItemId(int index) {
        return index;
    }

    public View getView(int index, View view, ViewGroup viewGroup) {
        CalendarEntry entry = entries.get(index);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.preview_entry, null);
        }
        ((TextView) view.findViewById(R.id.preview_date)).setText(DateUtils.formatDate(entry.begin, "dd.MM HH:mm"));
        ((TextView) view.findViewById(R.id.preview_title)).setText(entry.title);
        ((CheckBox) view.findViewById(R.id.preview_match)).setChecked(conditions.matches(entry));
        return view;
    }
}
