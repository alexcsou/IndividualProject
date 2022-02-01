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
    private String meetingDuration;
    private String transcriptRecognizability;
    private String language;

    public TranscriptHandler(Stage stage) {
        button.setOnAction(e -> chooseFile());
        this.stage = stage;
        sentences = new ArrayList<>();
        meetingDuration = "";
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

        ;
        return transcript;
    }

    public void handlefile() {
        // getGeneralMeetingData();
        generateSentences();
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

                } else if (line.contains("NOTE recognizability:")) {

                } else if (line.contains("NOTE language:")) {

                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("transcript file was not found");
        }
    }

    public TranscriptSentence generateSentence(String confidence, String duration, String text) {
        return new TranscriptSentence(text, 0, 0);
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
}
