package org.acm.steidinger.calendar.localePlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StopWords {
    private List<String> excludedWords;

    public StopWords(String exclusions) {
        this.excludedWords = getExcludedWords(exclusions);
    }

    public boolean containsExcludedWord(final String title) {
        String lowerCaseTitle = title == null ? "" : title.toLowerCase();
        boolean containsExcludedWord = false;
        for (String word : excludedWords) {
            if (word.length() > 0 && lowerCaseTitle.contains(word)) {
                containsExcludedWord = true;
            }
        }
        return containsExcludedWord;
    }

    private List<String> getExcludedWords(String exclusions) {
        List<String> excludedWords;
        if (exclusions != null && exclusions.trim().length() > 0) {
            String[] split = exclusions.split("(,|\\s)+");
            excludedWords = new ArrayList<String>(split.length);
            for (String word : split) {
                if (word != null && word.length() > 0) {
                    excludedWords.add(word.toLowerCase());
                }
            }
        } else {
            excludedWords = Collections.emptyList();
        }
        return excludedWords;
    }

    public List<String> getExcludedWords() {
        return excludedWords;
    }
}
