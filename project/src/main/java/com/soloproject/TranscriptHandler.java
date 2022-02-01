package com.soloproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javafx.scene.control.Button;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

import java.util.regex.*;

/**
 * A class to allow a user to select the trancript .vtt file they want analysed
 * by
 * Fixated.
 */
public class TranscriptHandler {

    private Button button = new Button("Choose a file");
    private Stage stage;
    private File transcript = null;
    private ArrayList<TranscriptSentence> sentences;
    private String meetingDurationString;
    private int meetingDurationSeconds;

    private String transcriptRecognizability;
    private String language;

    public TranscriptHandler(Stage stage) {
        button.setOnAction(e -> chooseFile());
        this.stage = stage;
        sentences = new ArrayList<>();
        meetingDurationString = "";
        meetingDurationSeconds = 0;
        transcriptRecognizability = "";
        language = "";
    }

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

    public void handlefile() {
        getGeneralMeetingData();
        // generateSentences();
    }

    public void generateSentences() {
        try {
            Scanner reader = new Scanner(transcript);
            ArrayList<String> groupof3 = new ArrayList<>();
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (isValid(line)) {
                    groupof3.add(line);
                    if (groupof3.size() == 3) {
                        sentences.add(generateSentence(groupof3.get(0), groupof3.get(1), groupof3.get(2)));
                        groupof3.clear();
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("transcript file was not found");
        }
    }

    public boolean isValid(String line) {
        return !(line.contains("WEBVTT") || line.contains("NOTE duration:") || line.contains("NOTE recognizability:")
                || line.contains("NOTE language:") || line.isBlank());
    }

    public void getGeneralMeetingData() {
        try {
            Scanner reader = new Scanner(transcript);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.contains("NOTE duration:")) {

                    Pattern pattern = Pattern.compile("\\d+:\\d+:\\d+");
                    Matcher matcher = pattern.matcher(line);
                    matcher.find();
                    if (matcher.find()) {
                        System.out.println("here");
                        setMeetingDurationString(matcher.group(0));
                        System.out.println(matcher.group(0));
                    }
                } else if (line.contains("NOTE recognizability:")) {

                    Pattern pattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+"); // from https: //
                                                                                  // stackoverflow.com/questions/12643009/regular-expression-for-floating-point-numbers/12643073
                    Matcher matcher = pattern.matcher(line);
                    matcher.find();
                    if (matcher.find()) {
                        System.out.println("here");
                        setTranscriptRecognizability(matcher.group(0));
                        System.out.println(matcher.group(0));
                    }
                } else if (line.contains("NOTE language:")) {

                    Pattern pattern = Pattern.compile("/(?!NOTE|language|:)([a-z0-9]+)");
                    Matcher matcher = pattern.matcher(line);
                    matcher.find();
                    if (matcher.find()) {
                        System.out.println("here");
                        setLanguage(matcher.group(0));
                        System.out.println(matcher.group(0));
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("transcript file was not found");
        } catch (Exception e) {
            System.out.println("Something went wrong...");
        }
    }

    public TranscriptSentence generateSentence(String confidence, String duration, String text) {

        return new TranscriptSentence(text, Double.parseDouble(duration), Double.parseDouble(confidence));
    }

    public Button getFileSelectButton() {
        return button;
    }

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

    public int getMeetingDurationSeconds() {
        return this.meetingDurationSeconds;
    }

    public void setMeetingDurationSeconds(int meetingDurationSeconds) {
        this.meetingDurationSeconds = meetingDurationSeconds;
    }
}
