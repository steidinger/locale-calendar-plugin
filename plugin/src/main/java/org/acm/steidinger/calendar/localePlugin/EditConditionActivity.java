// Copyright 2011 Frank Steidinger,
// derived from example code provided by two forty four a.m. LLC <http://www.twofortyfouram.com>
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
package org.acm.steidinger.calendar.localePlugin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.*;
import com.twofortyfouram.locale.BreadCrumber;
import com.twofortyfouram.locale.SharedResources;
import org.acm.steidinger.calendar.CalendarInfo;
import org.acm.steidinger.calendar.CalendarProvider;
import org.acm.steidinger.calendar.ConditionGroup;
import org.acm.steidinger.calendar.ConditionGroupBuilder;

import java.util.ArrayList;
import java.util.List;

public class EditConditionActivity extends Activity {
    /**
     * Help URL, used for the {@link R.id#twofortyfouram_locale_menu_help} menu item.
     */
    private static final String HELP_URL = "https://github.com/steidinger/locale-calendar-plugin";

    private boolean isCancelled = false;
    private List<CalendarInfo> calendars;
    private List<CheckBox> calendarCheckBoxes;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preventCustomSerializableAttack(getIntent());
        setContentView(R.layout.main);
        CharSequence title = BreadCrumber.generateBreadcrumb(getApplicationContext(), getIntent(), getString(R.string.plugin_name));
        setTitle(title);

        /*
         * Load the background frame from the host APK. Normally, the host APK should provide all of the necessary resources.
         * However, a non-compliant host APK could potentially not provide the needed resources. The null checks performed here
         * allow the use of default values, while also permitting the host APK to also customize the look-and-feel of the UI frame
         */
        final Drawable borderDrawable = SharedResources.getDrawableResource(getPackageManager(), getCallingPackage(), SharedResources.DRAWABLE_LOCALE_BORDER);
        if (borderDrawable == null) {
            // this is ugly, but it maintains compatibility
            findViewById(R.id.frame).setBackgroundColor(Color.WHITE);
        } else {
            findViewById(R.id.frame).setBackgroundDrawable(borderDrawable);
        }

        calendars = CalendarProvider.getCalendars(this);
        calendarCheckBoxes = new ArrayList<CheckBox>(calendars.size());
        ViewGroup calendarGroup = (ViewGroup) findViewById(R.id.calendarGroup);
        LayoutInflater inflater = this.getLayoutInflater();
        for (CalendarInfo calendar : calendars) {
            CheckBox box = (CheckBox) inflater.inflate(R.layout.calendar_line, null);
            box.setText(calendar.name);
            box.setTextColor(Color.BLACK);
            calendarGroup.addView(box);
            calendarCheckBoxes.add(box);
        }
        Spinner leadTimeSpinner = (Spinner) findViewById(R.id.leadTimeSpinner);
        ArrayAdapter<String> leadTimeAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{
                getString(R.string.lead_time_0min),
                getString(R.string.lead_time_5min),
                getString(R.string.lead_time_10min)
        });
        leadTimeSpinner.setAdapter(leadTimeAdapter);
        leadTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /*
         * if savedInstanceState == null, then then this is a new Activity instance and a check for EXTRA_BUNDLE is needed
         */
        if (savedInstanceState == null) {
            final Bundle forwardedBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);

            /*
             * the forwardedBundle would be null if this was a new condition
             */
            if (forwardedBundle != null) {
                ArrayList<String> selectedCalendarIds= null;
                if (forwardedBundle.containsKey(Constants.BUNDLE_EXTRA_CALENDAR_IDS)) {
                    selectedCalendarIds = forwardedBundle.getStringArrayList(Constants.BUNDLE_EXTRA_CALENDAR_IDS);
                }
                // compat to plugin version < 0.5
                if (selectedCalendarIds == null && forwardedBundle.containsKey(Constants.BUNDLE_EXTRA_CALENDAR_ID)) {
                    selectedCalendarIds = new ArrayList<String>(1);
                    selectedCalendarIds.add(forwardedBundle.getString(Constants.BUNDLE_EXTRA_CALENDAR_ID));
                }
                if (selectedCalendarIds != null && !selectedCalendarIds.isEmpty()) {
                    for (int i = 0; i < this.calendars.size(); i++) {
                        if (selectedCalendarIds.contains(calendars.get(i).id)) {
                            calendarCheckBoxes.get(i).setChecked(true);
                        }
                    }
                }
                int leadTime = forwardedBundle.getInt(Constants.BUNDLE_EXTRA_LEAD_TIME, 5);
                if (leadTime == 0) leadTimeSpinner.setSelection(0);
                else if (leadTime == 5) leadTimeSpinner.setSelection(1);
                else leadTimeSpinner.setSelection(2);
                String exclusions = forwardedBundle.getString(Constants.BUNDLE_EXTRA_EXCLUSION);
                if (exclusions != null) {
                    ((EditText) findViewById(R.id.exclusions)).setText(exclusions);
                }
                String inclusions = forwardedBundle.getString(Constants.BUNDLE_EXTRA_INCLUSION);
                if (inclusions != null) {
                    ((EditText) findViewById(R.id.inclusions)).setText(inclusions);
                }
                boolean ignoreAllDayEvents = forwardedBundle.getBoolean(Constants.BUNDLE_EXTRA_IGNORE_ALL_DAY_EVENTS, true);
                ((CheckBox) findViewById(R.id.allDayCheckbox)).setChecked(ignoreAllDayEvents);
            }
        }
        // quick fix for white label on white background.
        ((CheckBox) findViewById(R.id.allDayCheckbox)).setTextColor(Color.BLACK);
        /*
         * if savedInstanceState != null, there is no need to restore any Activity state directly via onSaveInstanceState()), as
         * the Spinner object handles that automatically
         */
    }

    public static void preventCustomSerializableAttack(final Intent intent) {
        /*
         * This is a hack to work around a custom serializable classloader attack. This check must come before any of the Intent
         * extras are examined.
         */
        try {
            final Bundle extras = intent.getExtras();

            if (extras != null) {
                // if a custom serializable exists, this will throw an exception
                extras.containsKey(null);
            }
        } catch (final Exception e) {
            Log.e(Constants.LOG_TAG, "Custom serializable attack detected; do not send custom Serializable subclasses to this Activity", e); //$NON-NLS-1$
            intent.replaceExtras((Bundle) null);
        }

        /*
         * Note: This is a hack to work around a custom serializable classloader attack via the EXTRA_BUNDLE. This check must come
         * before any of the Bundle extras are examined.
         */
        try {
            // if a custom serializable exists, this will throw an exception
            final Bundle extras = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);

            if (extras != null) {
                // if a custom serializable exists, this will throw an exception
                extras.containsKey(null);
            }
        } catch (final Exception e) {
            Log.e(Constants.LOG_TAG, "Custom serializable attack detected; do not send custom Serializable subclasses to this Activity", e); //$NON-NLS-1$
            intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, (Bundle) null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finish() {
        if (isCancelled) {
            setResult(RESULT_CANCELED);
        } else {
            final Spinner leadTimeSpinner = (Spinner) findViewById(R.id.leadTimeSpinner);
            final Intent returnIntent = new Intent();

            final Bundle storeAndForwardExtras = new Bundle();
            StringBuilder blurb = new StringBuilder();
            ArrayList<String> selectedCalendarIds = new ArrayList<String>(calendarCheckBoxes.size());
            ArrayList<String> names = new ArrayList<String>();
            for (int i = 0; i < calendarCheckBoxes.size(); i++) {
                if (calendarCheckBoxes.get(i).isChecked()) {
                    selectedCalendarIds.add(calendars.get(i).id);
                    names.add(calendars.get(i).name);
                }
            }
            storeAndForwardExtras.putStringArrayList(Constants.BUNDLE_EXTRA_CALENDAR_IDS, selectedCalendarIds);
            blurb.append(TextUtils.join(",", names));

            int leadTime = 5;
            switch (leadTimeSpinner.getSelectedItemPosition()) {
                case 0:
                    leadTime = 0;
                    break;
                case 1:
                    leadTime = 5;
                    blurb.append(" -").append(getString(R.string.lead_time_5min_short));
                    break;
                case 2:
                    leadTime = 10;
                    blurb.append(" -").append(getString(R.string.lead_time_10min_short));
                    break;
                default:
                    Log.w(Constants.LOG_TAG, "Fell through switch statement"); //$NON-NLS-1$
                    break;
            }
            storeAndForwardExtras.putInt(Constants.BUNDLE_EXTRA_LEAD_TIME, leadTime);
            String exclusions = ((EditText) findViewById(R.id.exclusions)).getText().toString();
            storeAndForwardExtras.putString(Constants.BUNDLE_EXTRA_EXCLUSION, exclusions);
            String inclusions = ((EditText) findViewById(R.id.inclusions)).getText().toString();
            storeAndForwardExtras.putString(Constants.BUNDLE_EXTRA_INCLUSION, inclusions);
            storeAndForwardExtras.putBoolean(Constants.BUNDLE_EXTRA_IGNORE_ALL_DAY_EVENTS, ((CheckBox) findViewById(R.id.allDayCheckbox)).isChecked());
            returnIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, blurb.toString());
            returnIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, storeAndForwardExtras);
            setResult(RESULT_OK, returnIntent);
        }

        super.finish();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        /*
         * inflate the default menu layout from XML
         */
        getMenuInflater().inflate(R.menu.twofortyfouram_locale_help_save_dontsave, menu);

        /*
         * Resources will be dynamically loaded from the host APK (e.g. Locale). Normally, the host APK should provide all of the
         * necessary resources. However, a non-compliant host APK could potentially not provide the needed resources. The null
         * checks performed here allow the use of default icons and strings from the inflated XML file, while also permitting the
         * host APK to also customize the look-and-feel of the menu items.
         */
        final PackageManager manager = getPackageManager();

        final MenuItem helpItem = menu.findItem(R.id.twofortyfouram_locale_menu_help);
        final CharSequence helpString = SharedResources.getTextResource(manager, getCallingPackage(), SharedResources.STRING_MENU_HELP);
        final Drawable helpIcon = SharedResources.getDrawableResource(manager, getCallingPackage(), SharedResources.DRAWABLE_MENU_HELP);
        if (helpString != null) {
            helpItem.setTitle(SharedResources.getTextResource(manager, getCallingPackage(), SharedResources.STRING_MENU_HELP));
        }
        if (helpIcon != null) {
            helpItem.setIcon(helpIcon);
        }

        final MenuItem dontSaveItem = menu.findItem(R.id.twofortyfouram_locale_menu_dontsave);
        final CharSequence dontSaveTitle = SharedResources.getTextResource(manager, getCallingPackage(), SharedResources.STRING_MENU_DONTSAVE);
        final Drawable dontSaveIcon = SharedResources.getDrawableResource(manager, getCallingPackage(), SharedResources.DRAWABLE_MENU_DONTSAVE);
        if (dontSaveTitle != null) {
            dontSaveItem.setTitle(dontSaveTitle);
        }
        if (dontSaveIcon != null) {
            dontSaveItem.setIcon(dontSaveIcon);
        }

        final MenuItem saveItem = menu.findItem(R.id.twofortyfouram_locale_menu_save);
        final CharSequence saveTitle = SharedResources.getTextResource(manager, getCallingPackage(), SharedResources.STRING_MENU_SAVE);
        final Drawable saveIcon = SharedResources.getDrawableResource(manager, getCallingPackage(), SharedResources.DRAWABLE_MENU_SAVE);
        if (saveTitle != null) {
            saveItem.setTitle(saveTitle);
        }
        if (saveIcon != null) {
            saveItem.setIcon(saveIcon);
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_preview: {
                Intent intent = new Intent(this, PreviewActivity.class);
                intent.putExtra("conditions", buildConditionGroup());
                startActivity(intent);
                return true;
            }
            case R.id.twofortyfouram_locale_menu_help: {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(HELP_URL)));
                } catch (final Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.twofortyfouram_locale_application_not_available, Toast.LENGTH_LONG).show();
                }

                return true;
            }
            case R.id.twofortyfouram_locale_menu_dontsave: {
                isCancelled = true;
                finish();
                return true;
            }
            case R.id.twofortyfouram_locale_menu_save: {
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private ConditionGroup buildConditionGroup() {
        ArrayList<String> selectedCalendarIds = new ArrayList<String>(calendarCheckBoxes.size());
        for (int i = 0; i < calendarCheckBoxes.size(); i++) {
            if (calendarCheckBoxes.get(i).isChecked()) {
                selectedCalendarIds.add(calendars.get(i).id);
            }
        }
        return ConditionGroupBuilder
                .all()
                .withCalendarIds(selectedCalendarIds)
                .containingWords(((TextView) this.findViewById(R.id.inclusions)).getText().toString())
                .notContainingWords(((TextView) this.findViewById(R.id.exclusions)).getText().toString())
                .ignoringAllDayEvents(((CheckBox) this.findViewById(R.id.allDayCheckbox)).isChecked())
                .build();
    }
}
