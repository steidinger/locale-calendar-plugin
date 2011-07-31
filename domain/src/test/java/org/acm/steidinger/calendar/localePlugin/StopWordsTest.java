package org.acm.steidinger.calendar.localePlugin;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class StopWordsTest {

    @Test
    public void getExcludedWords_shouldReturnEmptyArray_forInputNull() {
        StopWords stopWords = new StopWords(null);
        assertEquals(0, stopWords.getExcludedWords().size());
    }

    @Test
    public void getExcludedWords_shouldReturnEmptyArray_forEmptyString() {
        StopWords stopWords = new StopWords("");
        assertEquals(0, stopWords.getExcludedWords().size());
    }

    @Test
    public void getExcludedWords_shouldReturnOneElement_forStringContainingNoSeparator() {
        StopWords stopWords = new StopWords("test");
        assertEquals(1, stopWords.getExcludedWords().size());
    }

    @Test
    public void getExcludedWords_shouldReturnOriginalString_forStringContainingNoSeparator() {
        StopWords stopWords = new StopWords("test");
        assertEquals("test", stopWords.getExcludedWords().get(0));
    }

    @Test
    public void getExcludedWords_shouldReturnTwoElements_forStringContainingSeparator() {
        StopWords stopWords = new StopWords("test, another");
        assertEquals(2, stopWords.getExcludedWords().size());
    }

    @Test
    public void getExcludedWords_shouldReturnSeparatedWord_forStringContainingOneSeparator() {
        StopWords stopWords = new StopWords("test, another");
        List<String> excludedWords = stopWords.getExcludedWords();
        assertEquals("test", excludedWords.get(0));
        assertEquals("another", excludedWords.get(1));
    }

    @Test
    public void getExcludedWords_shouldReturnOneWord_forStringContainingSeparatorAtEnd() {
        StopWords stopWords = new StopWords("test, ");
        List<String> excludedWords = stopWords.getExcludedWords();
        assertEquals("test", excludedWords.get(0));
        assertEquals(1, excludedWords.size());
    }

    @Test
    public void getExcludedWords_shouldReturnOneWord_forStringContainingSeparatorAtBeginning() {
        StopWords stopWords = new StopWords(" test");
        List<String> excludedWords = stopWords.getExcludedWords();
        assertEquals("test", excludedWords.get(0));
        assertEquals(1, excludedWords.size());
    }
}
