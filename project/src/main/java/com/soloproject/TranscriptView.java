package com.soloproject;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * A view displayed in the transcript tab of the app, containing various
 * transcript analysis results and information.
 */
public class TranscriptView {

    private BorderPane mainPane;
    private TranscriptHandler handler;

    public TranscriptView(TranscriptHandler handler) {
        this.handler = handler;
        mainPane = new BorderPane();
        makeView();
    }

    public void makeView() {
        VBox chatBubbles = new VBox(new Label("test"));

        VBox meetingDetails = new VBox();

        Label meetingInformation = new Label("Meeting Information\n" + "_");
        meetingInformation.setTextAlignment(TextAlignment.CENTER);
        meetingInformation.getStyleClass().add("bigTitle");

        Label meetingName = new Label(
                "Meeting Name: " + handler.getTranscript().getName().replace("_AutoGeneratedCaption.vtt", ""));

        Label meetingDuration = new Label("Meeting Duration: " + handler.getMeetingDurationString());
        if (handler.getMeetingDurationString() == "") {
            meetingDuration.setText("Meeting Duration: not found.");
        }

        Label totalSeconds = new Label("Meeting duration in seconds: " + handler.getMeetingDurationSeconds());
        if (handler.getMeetingDurationSeconds() == 1) {
            // no value was found, 1 is passed to avoid div by 0

            totalSeconds.setText("Meeting duration in seconds: not found.");
        }

        Label meetingLanguage = new Label("Meeting language: " + handler.getLanguage());
        if (handler.getLanguage() == "") {
            meetingLanguage.setText("Meeting language: not found.");
        }

        Label sentenceCount = new Label("Number of spoken sentences: " + handler.getFullSentenceCount());
        if (handler.getFullSentenceCount() == 1) {
            // no sentences were found, 1 is passed to avoid div by 0
            sentenceCount.setText("Number of spoken sentences: not found.");
        }

        Label wordCount = new Label("Number of spoken words: " + handler.getNumberOfWords());
        if (handler.getNumberOfWords() == 0) {
            wordCount.setText("Number of spoken words: not found.");
        }

        Label meetingRecog = new Label(
                "Transcript accuracy: " + handler.getTranscriptRecognizabilityDouble() * 100 + "%");
        if (handler.getTranscriptRecognizabilityDouble() == 0.0) {
            meetingRecog.setText("Transcript accuracy: not found.");
        }

        Label wordsPerMinute = new Label(
                "Words spoken per minute: " + handler.getNumberOfWords() / (handler.getMeetingDurationSeconds() / 60));
        if (handler.getNumberOfWords() == 0 || handler.getMeetingDurationSeconds() == 0) {
            wordsPerMinute.setText("Words spoken per minute: not found.");
        }

        Label avgSentenceLength = new Label(
                "Average sentence length: " + handler.getNumberOfWords() / handler.getFullSentenceCount() + " words");
        if (handler.getNumberOfWords() == 0 || handler.getFullSentenceCount() == 0) {
            avgSentenceLength.setText("Average sentence length: not found.");
        }

        meetingDetails.getChildren().addAll(meetingInformation, meetingName, meetingLanguage, meetingDuration,
                totalSeconds, meetingRecog,
                wordCount, wordsPerMinute, sentenceCount, avgSentenceLength);
        meetingDetails.getStyleClass().add("transcriptVBox");
        meetingDetails.getChildren().forEach(c -> {
            if (c != meetingInformation) {
                c.getStyleClass().add("meetingInfo");
            }
        });

        mainPane.setCenter(chatBubbles);
        mainPane.setRight(meetingDetails);
    }

    public BorderPane getView() {
        return mainPane;
    }

}
