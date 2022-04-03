package com.soloproject;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.nlp.parser.shiftreduce.BinaryTransition.Side;

public class ParticipantTest {

    private Participant participant;

    @Before
    public void setUp() {
        participant = new Participant("Test Participant");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void assertInitialisedCorrectly() {
        assertEquals(participant.getName(), "Test Participant");
        assertEquals(0, participant.getSentences().size());
        assertEquals(0.0, participant.getAverageSentiment(), 0.0);
    }

    @Test
    public void assertAddSentenceWorks() {
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 0.0, "author"));
        assertEquals(1, participant.getSentences().size());
    }

    @Test
    public void assertgetSpokenTimeWorks1() {
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 200, "author"));
        assertEquals(200, participant.getSpokenTime(), 0.0);
    }

    @Test
    public void assertgetSpokenTimeWorks2() {
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 8, "author"));
        participant.addSentence(new TranscriptSentence("This is is a test", "", 16, 24, "author"));
        assertEquals(16, participant.getSpokenTime(), 0.0);
    }

    @Test
    public void assertgetSpokenTimeWorks3() {
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 0.99, "author"));
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 0, "author"));
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 0.0, "author"));
        assertEquals(0, participant.getSpokenTime(), 0.0);
    }

    @Test
    public void assertgetSpokenTimeWorks4() {
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 0.999, "author"));
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 0, "author"));
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 0.0, "author"));
        assertEquals(1, participant.getSpokenTime(), 0.0);
    }

    @Test
    public void assertgetSpokenTimeWorks5() {
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 0.0, "author"));
        assertEquals(0, participant.getSpokenTime(), 0.0);
    }

    @Test
    public void assertgetNumberofWordsWorks1() {
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 0.99, "author"));
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 0, "author"));
        participant.addSentence(new TranscriptSentence("This is is a test", "", 0.0, 0.0, "author"));
        assertEquals(15, participant.getNumberOfWords(), 0.0);
    }

    @Test
    public void assertgetNumberofWordsWorks2() {
        participant.addSentence(new TranscriptSentence("test", "", 0.0, 0.99, "author"));
        assertEquals(1, participant.getNumberOfWords(), 0.0);
    }

    @Test
    public void assertgetNumberofWordsWorks3() {
        assertEquals(0, participant.getNumberOfWords(), 0.0);
    }

    @Test
    public void assertGetWpmWorks1() {
        TranscriptHandler handler = new TranscriptHandler();
        participant.addSentence(new TranscriptSentence("test", "", 0.0, 60, "Test Participant"));

        handler.getParticipants().add(participant);
        handler.getParticipants().add(new Participant("test"));
        handler.setMeetingDurationSeconds(60);
        assertEquals(1, participant.getWpm(handler), 0);

    }

    @Test
    public void assertGetWpmWorks2() {
        TranscriptHandler handler = new TranscriptHandler();
        participant.addSentence(new TranscriptSentence("test test test", "", 0.0, 60, "Test Participant"));

        handler.getParticipants().add(participant);
        handler.setMeetingDurationSeconds(60);
        assertEquals(3, participant.getWpm(handler), 0);

    }

    @Test
    public void assertGetWpmWorks3() {
        TranscriptHandler handler = new TranscriptHandler();
        participant.addSentence(new TranscriptSentence("test test test", "", 0, 6, "Test Participant"));

        handler.getParticipants().add(participant);
        handler.setMeetingDurationSeconds(6);
        assertEquals(30, participant.getWpm(handler), 0);

    }

    @Test
    public void assertGetWpmWorks4() {
        TranscriptHandler handler = new TranscriptHandler();
        assertEquals(0, participant.getWpm(handler), 0);

    }

    @Test
    public void assertGetWpmWorks5() {
        TranscriptHandler handler = new TranscriptHandler();
        participant.addSentence(new TranscriptSentence("test test test", "", 0, 6, "Test Participant"));
        participant.addSentence(new TranscriptSentence("test test test", "", 7, 12, "Test Participant"));
        participant.addSentence(new TranscriptSentence("test test test", "", 12, 60, "Test Participant"));

        handler.getParticipants().add(participant);
        handler.setMeetingDurationSeconds(60);
        assertEquals(9, participant.getWpm(handler), 0);

    }

    @Test
    public void assertSetAverageSentimentWorks1() {
        participant
                .addSentence(new TranscriptSentence("Dark death terrible kill murder", "", 0, 6, "Test Participant"));
        participant.addSentence(
                new TranscriptSentence("happy joy love enjoy so great love you ", "", 7, 12, "Test Participant"));
        participant.getSentences().get(0).setSentenceSentiment();
        participant.getSentences().get(1).setSentenceSentiment();
        participant.setAverageSentiment();
        assertEquals(0, participant.getAverageSentiment(), 0.0);
    }

    @Test
    public void assertSetAverageSentimentWorks2() {
        participant.addSentence(
                new TranscriptSentence(
                        "Ok", "", 7, 12, "Test Participant"));
        participant.getSentences().get(0).setSentenceSentiment();
        participant.setAverageSentiment();
        assertEquals(0, participant.getAverageSentiment(), 0.0);
    }
}
