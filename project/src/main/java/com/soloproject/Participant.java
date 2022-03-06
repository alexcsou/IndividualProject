package com.soloproject;

import java.util.ArrayList;

public class Participant {

    private String name;
    private ArrayList<TranscriptSentence> sentences;
    private Double averageSentiment;

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

    public Double getWpm(TranscriptHandler handler) {

        int spokenTime = 0;
        for (Participant p : handler.getParticipants()) {
            spokenTime += p.getSpokenTime();
        }
        Double silenceTime = handler.getMeetingDurationSeconds() - spokenTime; // add up total silence time to spread an
                                                                               // equal share to all users to have a
                                                                               // more accurate wpm.

        Double silenceShare = silenceTime / handler.getParticipants().size();

        Long minutes = Math.round((getSpokenTime() + silenceShare) / 60);
        return getNumberOfWords() / minutes;

    }

    public void setAverageSentiment() {
        Double average = 0.0;
        for (TranscriptSentence s : sentences) {
            average += s.getSentimentRating();
        }
        averageSentiment = (average / sentences.size());
    }

    public Double getAverageSentiment() {
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

    public ArrayList<TranscriptSentence> getPositives() {
        ArrayList<TranscriptSentence> array = new ArrayList<>();

        for (TranscriptSentence s : sentences) {
            if (s.getSentimentRating() == 1) {
                array.add(s);
            }
        }
        return array;
    }

    public ArrayList<TranscriptSentence> getNeutrals() {
        ArrayList<TranscriptSentence> array = new ArrayList<>();

        for (TranscriptSentence s : sentences) {
            if (s.getSentimentRating() == 0) {
                array.add(s);
            }
        }
        return array;
    }

    public ArrayList<TranscriptSentence> getNegatives() {
        ArrayList<TranscriptSentence> array = new ArrayList<>();

        for (TranscriptSentence s : sentences) {
            if (s.getSentimentRating() == -1) {
                array.add(s);
            }
        }
        return array;
    }

    public int getHesitationCount() {
        int count = 0;
        for (TranscriptSentence s : sentences) {
            count += s.getHesitationWordsCount();
        }
        return count;
    }

    public int getFillerCount() {
        int count = 0;
        for (TranscriptSentence s : sentences) {

            count += s.getFillerWordsCount();
        }
        return count;
    }

    @Override
    public String toString() {
        return getName();
    }
}