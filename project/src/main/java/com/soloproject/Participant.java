package com.soloproject;

import java.util.ArrayList;

public class Participant {

    private TranscriptHandler handler;
    private String name;
    private ArrayList<TranscriptSentence> sentences;
    private ArrayList<String> speechTimeStamps; // arraylist of xx:xx:xx --> xx:xx:xx strings
    private Double spokenTime;
    private Double numberOfSentences;
    private Double numberOfWords;
    private Double wpm;
    private Long averageSentiment;

    public Participant(String name) {
        this.name = name;
        this.sentences = new ArrayList<>();
    }

    public void addSentence(TranscriptSentence sentence) {
        sentences.add(sentence);
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<TranscriptSentence> getSentences() {
        return this.sentences;
    }

    public Double getSpokenTime() {
        int total = 0;
        for (TranscriptSentence s : sentences) {
            total += s.getDuration();
        }
        return total * 1.0;
    }

    public Double getNumberOfSentences() {
        return sentences.size() * 1.0; // cast to Double
    }

    public Double getNumberOfWords() {
        int total = 0;
        for (TranscriptSentence s : sentences) {
            total += s.getNumberOfWords();
        }
        return total * 1.0;
    }

    public Double getWpm() {
        return 0.0;

    }

    public void setAverageSentiment() {
        Double average = 0.0;
        for (TranscriptSentence s : sentences) {
            average += s.getSentimentRating();
        }
        averageSentiment = Math.round(average / sentences.size());
    }

    public Long getAverageSentiment() {
        return this.averageSentiment;
    }

    /**
     * Used for debugging.
     */
    public void PrintSentences() {
        for (TranscriptSentence s : sentences) {
            System.out.println(s.getSentence());
        }
    }
}
