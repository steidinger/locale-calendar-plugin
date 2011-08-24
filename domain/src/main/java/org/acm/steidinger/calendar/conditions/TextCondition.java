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

package org.acm.steidinger.calendar.conditions;

import org.acm.steidinger.calendar.Condition;

public abstract class TextCondition extends Condition {
    public final String text;
    private final boolean checkIfContained;

    protected TextCondition(String text, boolean checkIfContained) {
        this.text = text.toLowerCase();
        this.checkIfContained = checkIfContained;
    }

    protected boolean matches(String actualText) {
        if (actualText == null) {
            return false;
        }
        boolean contained = actualText.toLowerCase().contains(text);
        if (checkIfContained) {
            return contained;
        } else {
            return !contained;
        }
    }
}
