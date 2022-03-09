package com.soloproject;

import java.util.ArrayList;

/**
 * The Participant class represents a person that attended and importantly spoke
 * during the meeting. It
 * houses and generates metrics about that user from the list of sentences
 * attributed to it. Particpants are used throughout the dashboard and insights
 * as to have their data and respective insights displayed.
 */
public class Participant {

    private String name;
    private ArrayList<TranscriptSentence> sentences; // populated via addSentence(). Sentences are added in transcript
                                                     // handler.
    private Double averageSentiment; // aevrage sentence sentiment. (-1 to 1)

    public Participant(String name) {
        this.name = name;
        this.sentences = new ArrayList<>();

    }

    /**
     * A method to add a sentence to this participant's list.
     * 
     * @param sentence the sentence to add
     */
    public void addSentence(TranscriptSentence sentence) {
        sentences.add(sentence);
    }

    /**
     * Returns the name of this participant
     * 
     * @return String the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the sentences of this Participant as an array list.
     * 
     * @return ArrayList of transcript sentences spoken by this participant.
     */
    public ArrayList<TranscriptSentence> getSentences() {
        return this.sentences;
    }

    /**
     * Returns the duration in seconds spoken by this participant by summing all
     * sentence durations
     * 
     * @return Douvle the spoken time in seconds.
     */
    public Double getSpokenTime() {
        int total = 0;
        for (TranscriptSentence s : sentences) {
            total += s.getDuration();
        }
        return total * 1.0;
    }

    /**
     * Returns the number of spoken sentences by this user
     * 
     * @return Double the number of spoken sentences
     */
    public Double getNumberOfSentences() {
        return sentences.size() * 1.0; // cast to Double
    }

    /**
     * Returns the number of spoken words by this user
     * 
     * @return Double the number of spoken words
     */
    public Double getNumberOfWords() {
        int total = 0;
        for (TranscriptSentence s : sentences) {
            total += s.getNumberOfWords();
        }
        return total * 1.0;
    }

    /**
     * A method that returns the average number of words spoken per minute for this
     * user. To account for the time spent in silence and not inflate wpm speeds by
     * simply dividing the number of spoken words by the number of spoken minutes,
     * the time spent in silence in the meeting is calculated and split evenly
     * amongst each participant.
     * 
     * @param handler the Transcripthandler used in the app; This is required as the
     *                handler is needed to get the participant list and meeting
     *                duration.
     * @return Double the wpm speed.
     */
    public Double getWpm(TranscriptHandler handler) {

        int spokenTime = 0; // traack total time spoken by all participants
        for (Participant p : handler.getParticipants()) {
            spokenTime += p.getSpokenTime();
        }

        // get time spent in silence as difference between meeting duration and spoken
        // time.
        Double silenceTime = handler.getMeetingDurationSeconds() - spokenTime;

        // calculate share of silence for each user.
        Double silenceShare = silenceTime / handler.getParticipants().size();

        // add up total silence time to spread an equal share to all users to have a
        // more accurate wpm.

        // return this participant's specific wpm speed.
        Long minutes = Math.round((getSpokenTime() + silenceShare) / 60);
        return getNumberOfWords() / minutes;

    }

    /**
     * Method that calculates and sets the averagesentiment field of this user by
     * dividing the sum of sentiment values by the number of sentences spoken by
     * this user.
     */
    public void setAverageSentiment() {
        Double average = 0.0;
        for (TranscriptSentence s : sentences) {
            average += s.getSentimentRating();
        }
        averageSentiment = (average / sentences.size());
    }

    /**
     * Getter for the averageSentiment field.
     * 
     * @return Double the average sentiment value of this user (-1 to 1)
     */
    public Double getAverageSentiment() {
        return this.averageSentiment;
    }

    /**
     * Method that prints out all of a Participant's sentences
     * Used for debugging.
     */
    public void PrintSentences() {
        for (TranscriptSentence s : sentences) {
            System.out.println(s.getSentence());
        }
    }

    /**
     * A method that returns the entire list of transcript sentences who's sentiment
     * is positive for this Participant.
     * 
     * @return an ArrayList of positive transcript sentences
     */
    public ArrayList<TranscriptSentence> getPositives() {
        ArrayList<TranscriptSentence> array = new ArrayList<>();

        for (TranscriptSentence s : sentences) {
            if (s.getSentimentRating() == 1) { // 1 = positive rating
                array.add(s);
            }
        }
        return array;
    }

    /**
     * A method that returns the entire list of transcript sentences who's sentiment
     * is neutral for this Participant.
     * 
     * @return an ArrayList of neutral transcript sentences
     */
    public ArrayList<TranscriptSentence> getNeutrals() {
        ArrayList<TranscriptSentence> array = new ArrayList<>();

        for (TranscriptSentence s : sentences) {
            if (s.getSentimentRating() == 0) { // 0 = neutral rating
                array.add(s);
            }
        }
        return array;
    }

    /**
     * A method that returns the entire list of transcript sentences who's sentiment
     * is negative for this Participant.
     * 
     * @return an ArrayList of negative transcript sentences
     */
    public ArrayList<TranscriptSentence> getNegatives() {
        ArrayList<TranscriptSentence> array = new ArrayList<>();

        for (TranscriptSentence s : sentences) {
            if (s.getSentimentRating() == -1) { // -1 = negative rating
                array.add(s);
            }
        }
        return array;
    }

    /**
     * A method that returns the list of declarative sentences for this participant
     * 
     * @return an arraylist of declarative sentences.
     */
    public ArrayList<TranscriptSentence> getDeclaratives() {
        ArrayList<TranscriptSentence> array = new ArrayList<>();
        for (TranscriptSentence s : sentences) {
            if (s.getSentenceType().equals("Declarative")) {
                array.add(s);
            }
        }
        return array;
    }

    /**
     * A method that returns the list of interrogative sentences for this
     * participant
     * 
     * @return an arraylist of interrogative sentences.
     */
    public ArrayList<TranscriptSentence> getInterrogatives() {
        ArrayList<TranscriptSentence> array = new ArrayList<>();
        for (TranscriptSentence s : sentences) {
            if (s.getSentenceType().equals("Interrogative")) {
                array.add(s);
            }
        }
        return array;
    }

    /**
     * A method that returns the list of exclamatory sentences for this participant
     * 
     * @return an arraylist of exclamatory sentences.
     */
    public ArrayList<TranscriptSentence> getExclamatories() {
        ArrayList<TranscriptSentence> array = new ArrayList<>();
        for (TranscriptSentence s : sentences) {
            if (s.getSentenceType().equals("Exclamatory")) {
                array.add(s);
            }
        }
        return array;
    }

    /**
     * A method that returns the count of hesitation words for this user.
     * 
     * @return int the number of hesitations
     */
    public int getHesitationCount() {
        int count = 0;
        for (TranscriptSentence s : sentences) {
            count += s.getHesitationWordsCount();
        }
        return count;
    }

    /**
     * A method that returns the count of filler words for this user.
     * 
     * @return int the number of filler words
     */
    public int getFillerCount() {
        int count = 0;
        for (TranscriptSentence s : sentences) {

            count += s.getFillerWordsCount();
        }
        return count;
    }

    /**
     * Oveeriden toString method as to have the participant name displayed in the
     * insightsview combobox instead of the object signature.
     */
    @Override
    public String toString() {
        return getName();
    }
}