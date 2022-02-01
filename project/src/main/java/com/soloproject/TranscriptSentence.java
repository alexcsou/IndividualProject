package com.soloproject;

public class TranscriptSentence {

    private String SENTENCE;
    private double DURATION;
    private double CONFIDENCE;

    public TranscriptSentence(String sentence, double duration, double confidence) {
        this.SENTENCE = sentence;
        this.DURATION = duration;
        this.CONFIDENCE = confidence;
    }

    public String getSentence() {
        return SENTENCE;
    }
}
