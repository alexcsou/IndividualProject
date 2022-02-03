package com.soloproject;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * TranscriptHandler is a class responsible for all operations to do with the
 * .vtt transcript provided by the user. It generates TranscriptSentence items
 * stored in a "sentences" arraylist. It also extracts the meeting duration and
 * the transcript recognizability, as well as its language. It also provides
 * the button that opens a file chooser.
 */
public class TranscriptHandler {

    private Button button;
    private Stage stage;
    private File transcript = null;
    private ArrayList<TranscriptSentence> sentences;
    private String meetingDurationString; // stored as string as displayed in .vtt file
    private double meetingDurationSeconds; // converts string duration to sum of seconds
    private String transcriptRecognizability; // value out of 1
    private String language; // usually en-us or en-uk.

    public TranscriptHandler(Stage stage) {
        button = new Button("Choose a file");
        button.setOnAction(e -> chooseFile());
        this.stage = stage;

        sentences = new ArrayList<>();
        meetingDurationString = "";
        meetingDurationSeconds = 0.0;
        transcriptRecognizability = "";
        language = "";
    }

    /**
     * A method to open a file chooser in an OS native look. Restricted to .vtt
     * files. Sets the transcript file and calls handlefile().
     */
    public File chooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a WebVTT file (.vtt)");

        chooser.getExtensionFilters().addAll(
                new ExtensionFilter("WebVTT Files", "*.vtt"));
        File newTranscript = chooser.showOpenDialog(stage);
        setTranscript(newTranscript);
        handlefile();

        return transcript;
    }

    /**
     * calls two big methods, the first which extracts some basic meeting data such
     * as duration, language or transcript confidence. The second generates the
     * array of TranscriptSentences.
     */
    public void handlefile() {
        generateMeetingData();
        generateSentences();
    }

    /**
     * Method which generates TranscriptSentences objetcs, which are composed of
     * lines extracted from the transcript: the confidence, the timestamp and the
     * text itself.
     */
    public void generateSentences() {
        try {
            Scanner reader = new Scanner(transcript);
            ArrayList<String> groupof3 = new ArrayList<>(); // create an array for storing lines during extraction
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (isValid(line)) { // check line isn't filler text or irrelevant
                    groupof3.add(line);
                    if (groupof3.size() == 3) { // as soon as the array reaches size 3, we create a sentence
                        sentences.add(generateSentence(groupof3.get(0), groupof3.get(1), groupof3.get(2)));
                        groupof3.clear(); // empty array and repeat until EOF
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("transcript file was not found");
        }
    }

    /**
     * A method to check if a line read from the .vtt file is either text, a time
     * stamp for the text line, or the confidence for that line. Excludes lines
     * giving general meeting data as well as ID lines which seem to appear on
     * transcripts for meetings lasting more than 1 min.
     * 
     * @param line the transcript line to check
     * @return true if the line is to be incorporated into a transcript sentence,
     *         false otherwise.
     */
    public boolean isValid(String line) {
        return !(line.contains("WEBVTT") || line.contains("NOTE duration:") || line.contains("NOTE recognizability:")
                || line.contains("NOTE language:") || isIDLine(line) || line.isBlank());
    }

    /**
     * A method that checks whether a transcript line is an ID line, which are
     * present on transcripts for meetings lasting over a few seconds (1min+).
     * An example of an ID line is : "dbcf586a-c9b6-461e-ba75-b76a72a8d398"
     * 
     * @param input the line to check
     * @return true if the line is an id line, false otherwise.
     */
    public boolean isIDLine(String input) {
        Pattern pattern = Pattern.compile("(.*?)-(.*?)-(.*?)-(.*?)-(.*?)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method that extracts and sets the meeting duration, transcript
     * recognizability and meeting language. Uses regex and the Pattern and Matcher
     * classes to identify them.
     */
    public void generateMeetingData() {
        boolean durationSet = false;
        boolean recogSet = false;
        boolean languageSet = false;
        try {
            Scanner reader = new Scanner(transcript);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.contains("NOTE duration:") && !durationSet) { // extra !durationSet condition in rare edge
                                                                       // cases where transcript contains the specified
                                                                       // string in line.contains()
                    Pattern pattern = Pattern.compile("\\d+:\\d+:\\d+");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        setMeetingDurationString(matcher.group(0)); // group(0) is the first match (and only match in
                                                                    // our case)
                        setMeetingDurationSeconds(secondParser(matcher.group(0))); // we have the duration string nicely
                                                                                   // formatted above, now we convert
                                                                                   // that duration string into a double
                                                                                   // amount of seconds
                        durationSet = true;
                    }
                } else if (line.contains("NOTE recognizability:") && !recogSet) {

                    Pattern pattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        setTranscriptRecognizability(matcher.group(0));
                        recogSet = true;
                    }
                } else if (line.contains("NOTE language:") && !languageSet) {

                    line = line.replace("NOTE language:", "");
                    setLanguage(line);
                    languageSet = true;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("transcript file was not found");
        } catch (Exception e) {
            System.out.println("Something went wrong...");
        }
    }

    /**
     * method inspired by: https://stackoverflow.com/q/34331637
     * A method to convert a string of the form "00:00:04.220" into a duration of
     * seconds.
     * 
     * @param input
     * @return
     */
    public double secondParser(String input) {
        String[] duration = input.split(":"); // split on : divider
        double HH = Double.parseDouble(duration[0]);
        double mm = Double.parseDouble(duration[1]);
        double ss = Double.parseDouble(duration[2]);
        return (HH * 3600 + mm * 60 + ss); // convert the hours and minutes to seconds, sum and return as double
    }

    /**
     * A method that creates a TranscriptSentence object from the provided strings.
     * Converts the timestamp line into an array of doubles, with index 0 as the
     * start and index 1 as the end. Also parses the double from the "confidence"
     * string
     * 
     * @param confidence a string for the AI transcripting confidence in the "text"
     * @param duration   a timestamp for the text line of the form "00:00:02.220 -->
     *                   00:00:04.220"
     * @param text       the spoken line as a string
     * @return a TranscriptSentence object with all its fields initialised.
     */
    public TranscriptSentence generateSentence(String confidence, String duration, String text) {
        ArrayList<Double> durationValues = getDurationDataToDouble(duration);
        return new TranscriptSentence(text, durationValues.get(0), durationValues.get(1),
                convertSentenceConfidence(confidence));
    }

    /**
     * A method to convert a time stamp (00:00:02.220 --> 00:00:04.220) into an
     * array of doubles of size two, with the values being the start and end times
     * of the timestamp in seconds since the start of the meeting.
     * 
     * @param durationLine a String of the form 00:00:02.220 --> 00:00:04.220
     * @return an arraylist of doubles with (if all goes well) two values, start and
     *         end of timestamp
     */
    public ArrayList<Double> getDurationDataToDouble(String durationLine) {
        ArrayList<Double> list = new ArrayList<>();
        String[] durations = durationLine.split("-->");
        try {
            double startTime = secondParser(durations[0]);
            double endTime = secondParser(durations[1]);
            list.add(startTime);
            list.add(endTime);
        } catch (NumberFormatException e) {
            System.out.println("getDurationDataToDouble failed");
            list.add(0.0);
            list.add(0.0);
            return list;
        }

        return list;
    }

    /**
     * A method to convert a string containing a double into a double. Extracted via
     * regex and then becomes the confidence value (out of 1) of a sentence.
     * 
     * @param input the sentence to extract the double from
     * @return
     */
    public double convertSentenceConfidence(String input) {
        Pattern pattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(0));
        } else {
            System.out.println("Confidence note error");
            return 0;
        }
    }

    // ------------------- Getters and Setters -------------------

    public Button getFileSelectButton() {
        return button;
    }

    /**
     * signals if transcript is null
     * 
     * @return the .vtt transcript file
     */
    public File getTranscript() {
        if (transcript == null) {
            System.out.println("Fetched null transcript file");
        }
        return transcript;
    }

    public void setTranscript(File file) {
        this.transcript = file;
    }

    public Button getButton() {
        return this.button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ArrayList<TranscriptSentence> getSentences() {
        return this.sentences;
    }

    public void setSentences(ArrayList<TranscriptSentence> sentences) {
        this.sentences = sentences;
    }

    public String getMeetingDurationString() {
        return this.meetingDurationString;
    }

    public void setMeetingDurationString(String meetingDurationString) {
        this.meetingDurationString = meetingDurationString;
    }

    public String getTranscriptRecognizability() {
        return this.transcriptRecognizability;
    }

    public void setTranscriptRecognizability(String transcriptRecognizability) {
        this.transcriptRecognizability = transcriptRecognizability;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double getMeetingDurationSeconds() {
        return this.meetingDurationSeconds;
    }

    public void setMeetingDurationSeconds(double meetingDurationSeconds) {
        this.meetingDurationSeconds = meetingDurationSeconds;
    }
}