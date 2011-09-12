package org.acm.steidinger.calendar;

import java.io.Serializable;

public interface Condition extends Serializable{
    boolean matches(CalendarEntry entry);

}
