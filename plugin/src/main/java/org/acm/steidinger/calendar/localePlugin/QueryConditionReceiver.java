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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import org.acm.steidinger.calendar.CalendarEntry;
import org.acm.steidinger.calendar.CalendarProvider;
import org.acm.steidinger.calendar.ConditionGroup;
import org.acm.steidinger.calendar.ConditionGroupBuilder;

import java.util.ArrayList;
import java.util.List;

public class QueryConditionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        debug("Querying Condition");
        if (!com.twofortyfouram.locale.Intent.ACTION_QUERY_CONDITION.equals(intent.getAction())) {
            Log.e(Constants.LOG_TAG, String.format("Received unexpected Intent action %s", intent.getAction())); //$NON-NLS-1$
            return;
        }
        EditConditionActivity.preventCustomSerializableAttack(intent);
        final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        if (!bundle.containsKey(Constants.BUNDLE_EXTRA_CALENDAR_STATE) ||
                (!bundle.containsKey(Constants.BUNDLE_EXTRA_CALENDAR_ID) && !bundle.containsKey(Constants.BUNDLE_EXTRA_CALENDAR_IDS))) {
            Log.e(Constants.LOG_TAG, "Missing param in Bundle"); //$NON-NLS-1$
            return;
        }
        boolean checkIfBooked = bundle.getBoolean(Constants.BUNDLE_EXTRA_CALENDAR_STATE, true);
        ArrayList<String> ids = null;
        if (bundle.containsKey(Constants.BUNDLE_EXTRA_CALENDAR_IDS)) {
            ids = bundle.getStringArrayList(Constants.BUNDLE_EXTRA_CALENDAR_IDS);
        }
        if (ids == null && bundle.containsKey(Constants.BUNDLE_EXTRA_CALENDAR_ID)) {
            ids = new ArrayList<String>(1);
            ids.add(bundle.getString(Constants.BUNDLE_EXTRA_CALENDAR_ID));
        }
        ConditionGroup condition = ConditionGroupBuilder.all()
                .withCalendarIds(ids)
                .withLeadTimeInMinutes(bundle.getInt(Constants.BUNDLE_EXTRA_LEAD_TIME, 5))
                .ignoringAllDayEvents(bundle.getBoolean(Constants.BUNDLE_EXTRA_IGNORE_ALL_DAY_EVENTS, true))
                .notContainingWords(bundle.getString(Constants.BUNDLE_EXTRA_EXCLUSION))
                .build();
        debug("Conditions:" + condition);
        if (ids == null || ids.isEmpty()) {
            return;
        }
        for (String id: ids) {
            List<CalendarEntry> entries = CalendarProvider.getNextCalendarEntries(context, id);
            debug(String.format("Checking %d calendar entries", entries.size()));
            boolean isBooked = condition.anyMatch(entries);
            if (checkIfBooked && isBooked) {
                debug("Found calendar entry -> Condition true");
                setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_SATISFIED);
                return;
            } else if (!checkIfBooked && !isBooked) {
                debug("Did not find calendar entry -> Condition true");
                setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_SATISFIED);
                return;
            }
        }
        debug("Condition false");
        setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNSATISFIED);
    }


    private void debug(final String msg) {
        if (Constants.IS_LOGGABLE) {
            Log.d(Constants.LOG_TAG, msg);
        }
    }
}
