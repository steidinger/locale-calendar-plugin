package org.acm.steidinger.calendar.localePlugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.StringBuilderPrinter;
import org.acm.steidinger.calendar.CalendarEntry;
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
        long leadTime = bundle.getInt(Constants.BUNDLE_EXTRA_LEAD_TIME, 5) * DateUtils.MINUTE_IN_MILLIS;
        String id = bundle.getString(Constants.BUNDLE_EXTRA_CALENDAR_ID);
        debug(String.format("Parameters: checkIfBooked=%s, calendarID=%s, leadTime=%d", checkIfBooked, id, leadTime));
        if (id == null) {
            return;
        }
        List<CalendarEntry> entries = CalendarProvider.getNextCalendarEntries(context, id);
        debug(String.format("Checking %d calendar entries", entries.size()));
        boolean isBooked = false;
        Date now = new Date();
        Date nowPlusLeadTime = new Date(now.getTime() + leadTime);
        debug("now=" + DateUtils.formatDateTime(context, now.getTime(), DateUtils.FORMAT_SHOW_TIME) + "  now+lead=" + DateUtils.formatDateTime(context, nowPlusLeadTime.getTime(), DateUtils.FORMAT_SHOW_TIME));
        String exclusions = bundle.getString(Constants.BUNDLE_EXTRA_EXCLUSION);
        String[] excludedWords = getExcludedWords(exclusions);

        for (CalendarEntry entry : entries) {
            if (entry.begin.before(nowPlusLeadTime) && entry.end.after(now)) {
                String lowerCaseTitle = entry.title == null ? "" : entry.title.toLowerCase();
                boolean isExcluded = false;
                for (String word : excludedWords) {
                    if (lowerCaseTitle.contains(word)) {
                        isExcluded = true;
                    }
                }
                isBooked = !isExcluded;
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

    private String[] getExcludedWords(String exclusions) {
        StringBuilder debugMsg = new StringBuilder("Excluded words: ");
        String[] excludedWords;
        if (exclusions != null) {
            excludedWords = exclusions.split("\\s+");
            for (int i = 0; i < excludedWords.length; i++) {
                excludedWords[i] = excludedWords[i].toLowerCase();
                if (Constants.IS_LOGGABLE) {
                    debugMsg.append(excludedWords[i]).append(", ");
                }
            }
        } else {
            excludedWords = new String[0];
        }
        debug(debugMsg.toString());
        return excludedWords;
    }

    private void debug(final String msg) {
        if (Constants.IS_LOGGABLE) {
            Log.d(Constants.LOG_TAG, msg);
        }
    }
}
