package com.soloproject;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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

        Label meetingInformation = new Label("Meeting Information\n" + "_");
        meetingInformation.setTextAlignment(TextAlignment.CENTER);
        meetingInformation.getStyleClass().add("bigTitle");
        VBox meetingDetails = new VBox(meetingInformation);
        VBox.setVgrow(meetingDetails, Priority.ALWAYS);

        mainPane.setCenter(chatBubbles);
        mainPane.setRight(meetingDetails);

        Label meetingName = new Label(
                "File Name: " + handler.getTranscript().getName().replace("_AutoGeneratedCaption.vtt", ""));
        Label meetingDuration = new Label("Meeting Duration: " + handler.getMeetingDurationString());
        Label totalSeconds = new Label("Meeting duration in seconds: " + handler.getMeetingDurationSeconds());
        Label meetingLanguage = new Label("Meeting language: " + handler.getLanguage());
        Label sentenceCount = new Label("Number of spoken sentences: " + handler.getFullSentenceCount());
        Label meetingRecog = new Label(
                "Transcript accuracy: " + handler.getTranscriptRecognizabilityDouble() * 100 + "%");

        meetingDetails.getChildren().addAll(meetingName, meetingDuration, totalSeconds, meetingRecog, sentenceCount,
                meetingLanguage);
        meetingDetails.getStyleClass().add("transcriptVBox");

    }

    public BorderPane getView() {
        return mainPane;
    }

}
