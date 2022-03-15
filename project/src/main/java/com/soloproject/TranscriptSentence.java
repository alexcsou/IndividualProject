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
        // TODO: edit here + ADD JAR TO README
        setSentenceSentiment();

        // populating arrays with all filler words and hesitation words
        populateHesitationWords();
        populateFillerWords();

    }

    /**
     * A method that returns the count of hesitation words found within a sentence.
     * 
     * @return int the count of hesitation words
     */
    public int getHesitationWordsCount() {
        int count = 0;
        String[] words = sentence.split(" ");
        for (String word : words) {
            if (hesitationWords.contains(word) // if the word is in the list, increment count. to handle issues with
                                               // words that are bound to a punction sign (e.g. 'uh,' since split on ' '
                                               // spaces), we
                                               // also look at words without their last character, if their length is
                                               // above two
                    || (word.length() >= 2 && (hesitationWords.contains(word.substring(0, word.length() - 1))))) {
                count++;
            }
        }
        return count;
    }

    /**
     * A method that returns the count of filler words found within a sentence.
     * 
     * @return int the count of filler words
     */
    public int getFillerWordsCount() {
        int count = 0;
        String[] words = sentence.split(" ");
        for (String word : words) {
            if (fillerWords.contains(word) // check above method for explanation
                    || (word.length() >= 2 && (fillerWords.contains(word.substring(0, word.length() - 1))))) {
                count++;
            }
        }
        return count;
    }

    /**
     * A method that attributes a sentence type to this sentence, based on it's last
     * character.
     */
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
     * sentiment to either neutral positive or negative for this sentence.
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

    /**
     * used for testing
     * 
     * @param sentiment the sentiment to set
     */
    public void setSentenceSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    /**
     * getter for the sentence sentiment
     * 
     * @return String the sentiment, either positive, neutral or negative
     */
    public String getSentiment() {
        return sentiment;
    }

    /**
     * A getter for the sentiment value as integer, binding negative to -1, neutral
     * to 0 and positive to 1.
     * 
     * @return the sentiment value of this sentence as an int. 0 if not set
     */
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

    /**
     * Getter
     * 
     * @return
     */
    public String getSentenceType() {
        return this.sentenceType;
    }

    /**
     * Getter
     * 
     * @return
     */
    public String getdurationString() {
        return this.durationString;
    }

    /**
     * Getter
     * 
     * @return
     */
    public double getStartTime() {
        return this.startTime;
    }

    /**
     * Setter for start time
     * 
     * @param startTime the double in seconds since sentence start time
     */
    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter
     * 
     * @return
     */
    public double getEndTime() {
        return this.endTime;
    }

    /**
     * Setter for end time
     * 
     * @param endTime the end time as double
     */
    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    /**
     * Getter for the string of the actual sentence
     * 
     * @return String the sentence spoken. empty string if sentence is null
     */
    public String getSentence() {
        if (sentence == null) {
            return "";
        } else {
            return sentence;
        }
    }

    /**
     * Setter for this sentence's string
     * 
     * @param sentence the String to set as sentence
     */
    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    /**
     * Getter
     * 
     * @return
     */
    public double getDuration() {
        return this.duration;
    }

    /**
     * Setter for sentence duration
     * 
     * @param duration duration in double of sentence
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Getter
     * 
     * @return
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Getter
     * 
     * @return
     */
    public int getNumberOfWords() {
        return this.numberOfWords;
    }

    /**
     * Method that populates the hesitation words array by adding a list of frequent
     * hesitation sounds or onomatopoeia picked up by the transcript. Both
     * capitalised and uncapitalised words are
     * added.
     */
    public void populateHesitationWords() {
        hesitationWords.add("Uhm");
        hesitationWords.add("uhm");
        hesitationWords.add("Eh");
        hesitationWords.add("eh");
        hesitationWords.add("Mhm");
        hesitationWords.add("mhm");
        hesitationWords.add("Hm");
        hesitationWords.add("hm");
        hesitationWords.add("Hmm");
        hesitationWords.add("hmm");
        hesitationWords.add("Uh");
        hesitationWords.add("uh");
        hesitationWords.add("Ah");
        hesitationWords.add("ah");
        hesitationWords.add("Ahh");
        hesitationWords.add("ahh");
        hesitationWords.add("Mh");
        hesitationWords.add("mh");
        hesitationWords.add("Um");
        hesitationWords.add("um");
        hesitationWords.add("Er");
        hesitationWords.add("er");
        hesitationWords.add("mm");
        hesitationWords.add("Mm");
        hesitationWords.add("Huh");
        hesitationWords.add("huh");
        hesitationWords.add("Uh-huh");
        hesitationWords.add("uh-huh");
        hesitationWords.add("Erm");
        hesitationWords.add("erm");
        hesitationWords.add("erm");
        hesitationWords.add("Uhu");
        hesitationWords.add("uhu");

    }

    /**
     * Method that populates the filler words list with often found and frequent
     * filler words used when speaking. Both capitalised and uncapitalised words are
     * added.
     */
    public void populateFillerWords() {
        fillerWords.add("Like");
        fillerWords.add("like");
        fillerWords.add("Kinda");
        fillerWords.add("kinda");
        fillerWords.add("basically");
        fillerWords.add("Basically");
        fillerWords.add("Literally");
        fillerWords.add("literally");
        fillerWords.add("seriously");
        fillerWords.add("Seriously");
        fillerWords.add("Actually");
        fillerWords.add("actually");
        fillerWords.add("Anyway");
        fillerWords.add("Anyways");
        fillerWords.add("anyway");
        fillerWords.add("anyways");
        fillerWords.add("Sorta");
        fillerWords.add("sorta");
        fillerWords.add("Yeah");
        fillerWords.add("yeah");
        fillerWords.add("Totally");
        fillerWords.add("totally");
        fillerWords.add("Hopefully");
        fillerWords.add("hopefully");
        fillerWords.add("Well");
        fillerWords.add("well");
        fillerWords.add("Pretty"); // Pretty much
        fillerWords.add("pretty");
        fillerWords.add("Whatever");
        fillerWords.add("whatever");
        fillerWords.add("Really");
        fillerWords.add("really");

    }
}
