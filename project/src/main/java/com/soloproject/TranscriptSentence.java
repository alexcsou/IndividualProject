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

    public String getSENTENCE() {
        return this.SENTENCE;
    }

    public void setSENTENCE(String SENTENCE) {
        this.SENTENCE = SENTENCE;
    }

    public double getDURATION() {
        return this.DURATION;
    }

    public void setDURATION(double DURATION) {
        this.DURATION = DURATION;
    }

    public double getCONFIDENCE() {
        return this.CONFIDENCE;
    }

    public void setCONFIDENCE(double CONFIDENCE) {
        this.CONFIDENCE = CONFIDENCE;
    }

}
