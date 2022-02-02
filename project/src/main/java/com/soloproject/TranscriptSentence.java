package com.soloproject;

public class TranscriptSentence {

    private String sentence;
    private double confidence;
    private double duration;
    private double startTime;
    private double endTime;

    public TranscriptSentence(String sentence, double startTime, double endTime, double confidence) {
        this.sentence = sentence;
        this.startTime = (double) Math.round(startTime * 100d) / 100d;
        this.endTime = (double) Math.round(endTime * 100d) / 100d;
        this.duration = (double) Math.round((endTime - startTime) * 100d) / 100d;
        this.confidence = (double) Math.round(confidence * 100d) / 100d;
        System.out.println(this.confidence + " " + this.duration + " " + this.startTime + " "
                + this.endTime);
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
