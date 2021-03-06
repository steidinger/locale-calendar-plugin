// Copyright 2011 Frank Steidinger
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

public interface Constants
{

	String LOG_TAG = "CalendarCondition";
    String BUNDLE_EXTRA_CALENDAR_STATE = "org.acm.steidinger.calendar.localePlugin.extra.STATE";
    String BUNDLE_EXTRA_CALENDAR_ID = "org.acm.steidinger.calendar.localePlugin.extra.ID";
    String BUNDLE_EXTRA_CALENDAR_IDS = "org.acm.steidinger.calendar.localePlugin.extra.IDS";
    String BUNDLE_EXTRA_LEAD_TIME = "org.acm.steidinger.calendar.localePlugin.extra.LEAD_TIME";
    String BUNDLE_EXTRA_EXCLUSION = "org.acm.steidinger.calendar.localePlugin.extra.EXCLUSION";
    String BUNDLE_EXTRA_INCLUSION = "org.acm.steidinger.calendar.localePlugin.extra.INCLUSION";
    String BUNDLE_EXTRA_STATUS = "org.acm.steidinger.calendar.localePlugin.extra.AVAILABILITY";
    String BUNDLE_EXTRA_LOCATION_EXCLUSION = "org.acm.steidinger.calendar.localePlugin.extra.LOCATION_EXCLUSION";
    String BUNDLE_EXTRA_LOCATION_INCLUSION = "org.acm.steidinger.calendar.localePlugin.extra.LOCATION_INCLUSION";
    String BUNDLE_EXTRA_IGNORE_ALL_DAY_EVENTS = "org.acm.steidinger.calendar.localePlugin.extra.ALL_DAY_EVENTS";

    boolean IS_LOGGABLE = true;
    boolean IS_DEBUG = false;
}
