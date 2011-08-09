package org.acm.steidinger.calendar;

import java.io.Serializable;

public abstract class Condition implements Serializable{
    public abstract boolean matches(CalendarEntry entry);

}
