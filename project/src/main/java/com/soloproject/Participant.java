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

    /**
     * Used for debugging.
     */
    public void PrintSentences() {
        for (TranscriptSentence s : sentences) {
            System.out.println(s.getSentence());
        }
    }
}
