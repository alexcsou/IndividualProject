package com.soloproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * the TranscriptSentence class represents a portion of a .vtt transcript stored
 * neatly together, namely the actual sentence spoken; the author, the sentence
 * start times and end times in seconds since the start of the recording, and
 * the duration of the sentence. Transcript sentences are generated in
 * transcript handler.
 */
public class TranscriptSentence {

    private String sentence;
    private String durationString;
    private double duration;
    private double startTime;
    private double endTime;
    private String author;
    private int numberOfWords;
    private String sentenceType;
    private String sentiment;
    private ArrayList<String> hesitationWords = new ArrayList<>();
    private ArrayList<String> fillerWords = new ArrayList<>();

    public TranscriptSentence(String sentence, String durationstring, double startTime, double endTime, String author) {
        this.sentence = sentence;
        this.durationString = durationstring;
        this.startTime = (double) Math.round(startTime * 100d) / 100d; // rounding all doubles to 3 digits. These values
                                                                       // are not sensitive and roudning errors in
                                                                       // duration or confidence will have little effect
        this.endTime = (double) Math.round(endTime * 100d) / 100d;
        this.duration = (double) Math.round((endTime - startTime) * 100d) / 100d; // duration is end - start
        this.author = author;
        this.numberOfWords = sentence.split(" ").length;
        setSentenceType();
        // setSentenceSentiment();
        // TODO: edit here
        this.sentiment = "Neutral";

        // populating arrays with all filler words and hesitation words
        hesitationWords.add("Uhm");
        hesitationWords.add("uhm");
        hesitationWords.add("Eh");
        hesitationWords.add("eh");
        hesitationWords.add("Mhm");
        hesitationWords.add("mhm");
        hesitationWords.add("Hm");
        hesitationWords.add("hm");
        hesitationWords.add("Uh");
        hesitationWords.add("uh");
        hesitationWords.add("Ah");
        hesitationWords.add("ah");
        hesitationWords.add("Mh");
        hesitationWords.add("mh");
        hesitationWords.add("Um");
        hesitationWords.add("Um");
        hesitationWords.add("Er");
        hesitationWords.add("er");
        hesitationWords.add("mm");
        hesitationWords.add("Mm");
        hesitationWords.add("Huh");
        hesitationWords.add("huh");
        hesitationWords.add("Uh-huh");
        hesitationWords.add("uh-huh");

    }

    public int getHesitationWordsCount() {
        int count = 0;
        String[] words = sentence.split(" ");
        for (String word : words) {
            if (hesitationWords.contains(word)
                    || (word.length() >= 2 && (hesitationWords.contains(word.substring(0, word.length() - 2))))) {
                count++;
            }
        }
        return count;
    }

    public int getFillerWordsCount() {
        int count = 0;
        String[] words = sentence.split(" ");
        for (String word : words) {
            if (fillerWords.contains(word)
                    || (word.length() >= 2 && (fillerWords.contains(word.substring(0, word.length() - 2))))) {
                count++;
            }
        }
        return count;
    }

    public void setSentenceType() {
        if (sentence.endsWith(".") || sentence.endsWith("...")) {
            sentenceType = "Declarative";
        } else if (sentence.endsWith("!")) {
            sentenceType = "Exclamatory";
        } else if (sentence.endsWith("?")) {
            sentenceType = "Interrogative";
        } else {
            sentenceType = "Other";
        }
    }

    /**
     * A method to provide the sentimental analysis for the sentence. Uses
     * StandfordCoreNLP.
     * guided and inspired by: https://www.youtube.com/watch?v=zjop7sE3g8I. Sets
     * sentiment to either neutral positive or negative
     */
    public void setSentenceSentiment() {

        Properties properties = new Properties();
        properties.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, sentiment");
        StanfordCoreNLP SFCNLP = new StanfordCoreNLP(properties);

        CoreDocument coreDocument = new CoreDocument(getSentence());
        SFCNLP.annotate(coreDocument);

        List<CoreSentence> sentenceList = coreDocument.sentences();

        for (CoreSentence s : sentenceList) {
            String sentiment = s.sentiment();

            this.sentiment = sentiment;
        }
    }

    public String getSentiment() {
        return sentiment;
    }

    public int getSentimentRating() {
        if (this.sentiment.equals("Negative")) {
            return -1;
        } else if (this.sentiment.equals("Neutral")) {
            return 0;
        } else if (this.sentiment.equals("Positive")) {
            return 1;
        } else {
            return 0;
        }
    }

    public String getSentenceType() {
        return this.sentenceType;
    }

    public String getdurationString() {
        return this.durationString;
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
        if (sentence == null) {
            return "";
        } else {
            return sentence;
        }
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

    public String getAuthor() {
        return this.author;
    }

    public int getNumberOfWords() {
        return this.numberOfWords;
    }
}
