package com.soloproject;

/**
 * the TranscriptSentence class represents a portion of a .vtt transcript stored
 * neatly together, namely the actual sentence or half sentence spoken the
 * confidence of the AI recognising it out of 1, the sentence start times and
 * end times in seconds since the start of the recording, and the duration of
 * the sentence.
 * Transcript setences are generated in transcript handler.
 */
public class TranscriptSentence {

    private String sentence;
    private double confidence;
    private double duration;
    private double startTime;
    private double endTime;

    public TranscriptSentence(String sentence, double startTime, double endTime, double confidence) {
        this.sentence = sentence;
        this.startTime = (double) Math.round(startTime * 100d) / 100d; // rounding all doubles to 3 digits. These values
                                                                       // are not sensitive and roudning errors in
                                                                       // duration or confidence will have little effect
        this.endTime = (double) Math.round(endTime * 100d) / 100d;
        this.duration = (double) Math.round((endTime - startTime) * 100d) / 100d; // duration is end - start
        this.confidence = (double) Math.round(confidence * 100d) / 100d;

    }

    public double getStartTime() {
        return this.startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return this.endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public String getSentence() {
        return this.sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public double getDuration() {
        return this.duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getConfidence() {
        return this.confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
