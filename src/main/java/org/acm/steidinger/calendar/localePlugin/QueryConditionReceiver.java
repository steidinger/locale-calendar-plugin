package org.acm.steidinger.calendar.localePlugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import org.acm.steidinger.calendar.CalendarProvider;

import java.util.Date;
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
        if (!bundle.containsKey(Constants.BUNDLE_EXTRA_CALENDAR_STATE) || !bundle.containsKey(Constants.BUNDLE_EXTRA_CALENDAR_ID)) {
            Log.e(Constants.LOG_TAG, "Missing param in Bundle"); //$NON-NLS-1$
            return;
        }
        boolean checkIfBooked = bundle.getBoolean(Constants.BUNDLE_EXTRA_CALENDAR_STATE, true);
        String id = bundle.getString(Constants.BUNDLE_EXTRA_CALENDAR_ID);
        debug(String.format("Parameters: checkIfBooked=%s, calendarID=%s", checkIfBooked, id));
        if (id == null) {
            return;
        }
        List<CalendarProvider.CalendarEntry> entries = CalendarProvider.getNextCalendarEntries(context, id);
        debug(String.format("Checking %d calendar entries", entries.size()));
        boolean isBooked = false;
        for (CalendarProvider.CalendarEntry entry : entries) {
            Date now = new Date();
            if (entry.begin.before(now) && entry.end.after(now)) {
                isBooked = true;
            }
        }
        if (checkIfBooked && isBooked) {
            debug("Found calendar entry -> Condition true");
            setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_SATISFIED);
        } else if (!checkIfBooked && !isBooked) {
            debug("Did not find calendar entry -> Condition true");
            setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_SATISFIED);
        } else {
            debug("Condition false");
            setResultCode(com.twofortyfouram.locale.Intent.RESULT_CONDITION_UNSATISFIED);
        }
    }

    private void debug(final String msg) {
        if (Constants.IS_LOGGABLE) {
            Log.d(Constants.LOG_TAG, msg);
        }
    }
}
