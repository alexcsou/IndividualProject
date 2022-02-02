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
    private double meetingDurationSeconds;
    private String transcriptRecognizability;
    private String language;

    public TranscriptHandler(Stage stage) {
        button.setOnAction(e -> chooseFile());
        this.stage = stage;
        sentences = new ArrayList<>();
        meetingDurationString = "";
        meetingDurationSeconds = 0.0;
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
        generateMeetingData();
        generateSentences();
        for (TranscriptSentence s : sentences) {
            System.out.println("NEW SETENCE:");
            System.out.println(s.getSentence());
            System.out.println(s.getConfidence());
            System.out.println(s.getStartTime());
            System.out.println(s.getEndTime());
            System.out.println(s.getDuration());
            System.out.println("");
        }
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

    public void generateMeetingData() {
        boolean durationSet = false;
        boolean recogSet = false;
        boolean languageSet = false;
        try {
            Scanner reader = new Scanner(transcript);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.contains("NOTE duration:") && !durationSet) {
                    Pattern pattern = Pattern.compile("\\d+:\\d+:\\d+");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        setMeetingDurationString(matcher.group(0));
                        setMeetingDurationSeconds(secondParser(matcher.group(0)));
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
     * 
     * @param input
     * @return
     */
    public double secondParser(String input) {
        String[] duration = input.split(":");
        double HH = Double.parseDouble(duration[0]);
        double mm = Double.parseDouble(duration[1]);
        double ss = Double.parseDouble(duration[2]);
        return (HH * 3600 + mm * 60 + ss);
    }

    public TranscriptSentence generateSentence(String confidence, String duration, String text) {
        ArrayList<Double> durationValues = getDurationDataToDouble(duration);
        return new TranscriptSentence(text, durationValues.get(0), durationValues.get(1),
                convertSentenceConfidence(confidence));
    }

    public ArrayList<Double> getDurationDataToDouble(String durationLine) {
        ArrayList<Double> list = new ArrayList<>();
        String[] durations = durationLine.split("-->");
        double startTime = secondParser(durations[0]);
        double endTime = secondParser(durations[1]);
        list.add(startTime);
        list.add(endTime);
        return list;
    }

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

    public Double getMeetingDurationSeconds() {
        return this.meetingDurationSeconds;
    }

    public void setMeetingDurationSeconds(double meetingDurationSeconds) {
        this.meetingDurationSeconds = meetingDurationSeconds;
    }
}
