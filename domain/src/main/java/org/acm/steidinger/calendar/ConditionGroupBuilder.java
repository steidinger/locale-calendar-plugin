package org.acm.steidinger.calendar;

import org.acm.steidinger.calendar.conditions.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConditionGroupBuilder {
    private ConditionGroup group;

    private ConditionGroupBuilder() {
        group = new ConditionGroup();
    }

    public static ConditionGroupBuilder all() {
        return new ConditionGroupBuilder();
    }

    public ConditionGroupBuilder withCalendarId(String id) {
        group.conditions.add(0, new BelongsToCalendar(id));
        return this;
    }

    public ConditionGroupBuilder withCalendarIds(ArrayList<String> ids) {
        group.conditions.add(0, new BelongsToCalendar(ids));
        return this;
    }

    public ConditionGroupBuilder withLeadTimeInMinutes(int minutes) {
        group.conditions.add(new TimeCondition(minutes));
        return this;
    }

    public ConditionGroupBuilder ignoringAllDayEvents(boolean ignore) {
        if (ignore) {
            group.conditions.add(new NotAllDayEvent());
        }
        return this;
    }

    public ConditionGroupBuilder titleNotContainingWords(String words) {
        if (words != null && words.trim().length() > 0) {
            String[] split = parseText(words);
            for (String word : split) {
                group.conditions.add(new TitleDoesNotContainText(word));
            }
        }
        return this;
    }

    public ConditionGroupBuilder titleContainingWords(String words) {
        if (words != null && words.trim().length() > 0) {
            String[] split = parseText(words);
            if (split.length == 1) {
                group.conditions.add(new TitleDoesContainText(split[0]));
            } else if (split.length > 1) {
                DisjunctiveConditionGroup disjunction = new DisjunctiveConditionGroup();
                for (String word : split) {
                    disjunction.conditions.add(new TitleDoesContainText(word));
                }
                group.conditions.add(disjunction);
            }
        }
        return this;
    }
    public ConditionGroupBuilder locationNotContainingWords(String words) {
        if (words != null && words.trim().length() > 0) {
            String[] split = parseText(words);
            for (String word : split) {
                group.conditions.add(new LocationDoesNotContainText(word));
            }
        }
        return this;
    }

    public ConditionGroupBuilder locationContainingWords(String words) {
        if (words != null && words.trim().length() > 0) {
            String[] split = parseText(words);
            if (split.length == 1) {
                group.conditions.add(new LocationDoesContainText(split[0]));
            } else if (split.length > 1) {
                DisjunctiveConditionGroup disjunction = new DisjunctiveConditionGroup();
                for (String word : split) {
                    disjunction.conditions.add(new LocationDoesContainText(word));
                }
                group.conditions.add(disjunction);
            }
        }
        return this;
    }

    protected static String[] parseText(String words) {
        if (words.isEmpty()) {
            return new String[] {""};
        }
        return new Parser(words).parse();
    }
    

    private static class Parser {
        private String text;
        private int position;
        
        Parser(String text) {
            this.text = text;
            position = 0;
        }
        
        String[] parse() {
            List<String> tokens = new LinkedList<String>();
            String next;
            while((next = nextToken()) != null) {
                tokens.add(next);
            }
            return tokens.toArray(new String[tokens.size()]);
        }
        
        private String nextToken() {
            String result;
            skipWhiteSpaceAndSeparator();
            if (position >= text.length()) {
                return null;
            }
            if (text.charAt(position) == '"' && position + 1 < text.length()) {
                int endOfPhrase = text.indexOf('"', position + 1);
                if (endOfPhrase != -1) {
                    result = text.substring(position + 1, endOfPhrase);
                    position = endOfPhrase + 1;
                }
                else {
                    result = text.substring(position);
                    position = text.length();
                }
            }
            else {
                int nextComma = text.indexOf(",", position);
                int nextSpace = text.indexOf(" ", position);
                int endOfWord;
                if (nextComma == -1 && nextSpace == -1) {
                    endOfWord = text.length();
                }
                else if (nextComma == -1) {
                    endOfWord = nextSpace;
                }
                else if (nextSpace == -1) {
                    endOfWord = nextComma;
                }
                else {
                    endOfWord = Math.min(nextComma, nextSpace);
                }
                result = text.substring(position, endOfWord);
                position = endOfWord + 1;
            }
            return result;
        }
        
        private void skipWhiteSpaceAndSeparator() {
            while (position < text.length() && isWhiteSpace(text.charAt(position))) {
                position += 1;
            }
        }

        private boolean isWhiteSpace(char c) {
            return c == ' ' || c == ',';
        }
    }
    public ConditionGroupBuilder withAvailability(int status) {
        if (status == CalendarEntry.STATUS_BUSY || status == CalendarEntry.STATUS_FREE) {
            group.conditions.add(new AvailabilityCondition(status));
        }
        return this;
    }

    public ConditionGroup build() {
        return group;
    }
    
}
