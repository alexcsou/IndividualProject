package com.soloproject;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TranscriptSentenceTest {
    private TranscriptSentence sentence;

    @Before
    public void setUp() {
        sentence = new TranscriptSentence("This is a test.", "00:00:01.000 --> 00:00:010.000", 1, 10,
                "Test Author");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void assertInitialisedCorrectly() {
        assertEquals("This is a test.", sentence.getSentence());
        assertEquals("Test Author", sentence.getAuthor());
        assertEquals("00:00:01.000 --> 00:00:010.000", sentence.getdurationString());
        assertEquals(1, sentence.getStartTime(), 0.0);
        assertEquals(10, sentence.getEndTime(), 0.0);
        assertEquals(9, sentence.getDuration(), 0.0);
        assertEquals(4, sentence.getNumberOfWords(), 0.0);
    }

    @Test
    public void assertSetSentenceTypeWorks1() {
        TranscriptSentence sentence1 = new TranscriptSentence("This is a test.", "00:00:01.000 --> 00:00:010.000", 1,
                10,
                "Test Author");
        assertEquals("Declarative", sentence1.getSentenceType());
    }

    @Test
    public void assertSetSentenceTypeWorks2() {
        TranscriptSentence sentence1 = new TranscriptSentence("This is a test!", "00:00:01.000 --> 00:00:010.000", 1,
                10,
                "Test Author");
        assertEquals("Exclamatory", sentence1.getSentenceType());
    }

    @Test
    public void assertSetSentenceTypeWorks3() {
        TranscriptSentence sentence1 = new TranscriptSentence("This is a test?", "00:00:01.000 --> 00:00:010.000", 1,
                10,
                "Test Author");
        assertEquals("Interrogative", sentence1.getSentenceType());
    }

    @Test
    public void assertSetSentenceTypeWorks4() {
        TranscriptSentence sentence1 = new TranscriptSentence("This is a test...", "00:00:01.000 --> 00:00:010.000", 1,
                10,
                "Test Author");
        assertEquals("Declarative", sentence1.getSentenceType());
    }

    @Test
    public void assertSetSentenceTypeWorks5() {
        TranscriptSentence sentence1 = new TranscriptSentence("This is a test", "00:00:01.000 --> 00:00:010.000", 1, 10,
                "Test Author");
        assertEquals("Other", sentence1.getSentenceType());
    }

    // setsentencesentiment tested in ParticipantTest

    @Test
    public void assertGetSentimentRatingWorks1() {
        sentence.setSentenceSentiment("Negative");
        assertEquals(-1, sentence.getSentimentRating());
    }

    @Test
    public void assertGetSentimentRatingWorks2() {
        sentence.setSentenceSentiment("Neutral");
        assertEquals(0, sentence.getSentimentRating());
    }

    @Test
    public void assertGetSentimentRatingWorks3() {
        sentence.setSentenceSentiment("Positive");
        assertEquals(1, sentence.getSentimentRating());
    }

    @Test
    public void assertGetSentimentRatingWorks4() {
        sentence.setSentenceSentiment("blahblah");
        assertEquals(0, sentence.getSentimentRating());
    }

    @Test
    public void assertPopulateHesitationWordsWorks1() {
        TranscriptSentence sentence1 = new TranscriptSentence("This is uhm, a test.", "", 1, 10, "Test Author");
        assertEquals(1, sentence1.getHesitationWordsCount());
    }

    @Test
    public void assertPopulateHesitationWordsWorks2() {
        TranscriptSentence sentence1 = new TranscriptSentence("Uhm! This is er, a Hummer.", "", 1, 10, "Test Author");
        assertEquals(2, sentence1.getHesitationWordsCount());
    }

    @Test
    public void assertPopulateHesitationWordsWorks3() {
        TranscriptSentence sentence1 = new TranscriptSentence("", "", 1, 10, "Test Author");
        assertEquals(0, sentence1.getHesitationWordsCount());
    }

    @Test
    public void assertPopulateFillerWordsWorks1() {
        TranscriptSentence sentence1 = new TranscriptSentence("This is Kinda like, a test.", "", 1, 10, "Test Author");
        assertEquals(2, sentence1.getFillerWordsCount());
    }

    @Test
    public void assertPopulateFillerWordsWorks2() {
        TranscriptSentence sentence1 = new TranscriptSentence("SortaAnyways this is Whatever!", "", 1, 10,
                "Test Author");
        assertEquals(1, sentence1.getFillerWordsCount());
    }

    @Test
    public void assertPopulateFillerWordsWorks3() {
        TranscriptSentence sentence1 = new TranscriptSentence("", "", 1, 10, "Test Author");
        assertEquals(0, sentence1.getFillerWordsCount());
    }
}
