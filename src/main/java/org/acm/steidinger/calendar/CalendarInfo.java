package org.acm.steidinger.calendar;

public class CalendarInfo {
    public String id;
    public String name;
    public boolean selected;

    public CalendarInfo(String id, String name, String selected) {
        this.id = id;
        this.name = name;
        this.selected = !"0".equals(selected);
    }

    @Override
    public String toString() {
        return name + " (@" + id + ")" + (selected ? " - enabled" : " - disabled");
    }
}
