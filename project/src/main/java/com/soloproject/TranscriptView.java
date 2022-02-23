package com.soloproject;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
    private AlertHandler alertHandler;

    public TranscriptView(TranscriptHandler handler) {
        this.handler = handler;
        this.alertHandler = new AlertHandler();
        mainPane = new BorderPane();
        makeView();
    }

    /**
     * Method that returns a provided transcript bubble in an HBox, with a specified
     * alignement.
     * Used to have the chat be formatted correctly.
     * 
     * @param alignement a string used like a boolean to determine the alignement
     * @param bubble     the transcript bubble to put in the HBox
     * @return the formatted HBox
     */
    public HBox getHBox(String alignement, TranscriptBubble bubble) {
        if (alignement.contentEquals("left")) {
            HBox box = new HBox(bubble.getBubble());
            box.setAlignment(Pos.BASELINE_RIGHT);
            HBox.setHgrow(bubble.getBubble(), Priority.ALWAYS);
            return box;
        } else {
            HBox box = new HBox(bubble.getBubble());
            box.setAlignment(Pos.BASELINE_LEFT);
            HBox.setHgrow(bubble.getBubble(), Priority.ALWAYS);
            return box;
        }
    }

    public void makeView() {

        ScrollPane chatBubbles = new ScrollPane();
        chatBubbles.getStyleClass().add("TranscriptScrollPane");
        chatBubbles.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        chatBubbles.setHbarPolicy(ScrollBarPolicy.NEVER);
        chatBubbles.setMinHeight(ScreenSizehandler.getHeight() * 0.90);
        chatBubbles.setMaxHeight(ScreenSizehandler.getHeight() * 0.75);

        VBox chatBubblesVBox = new VBox();

        // style bubble alignment in alternating fashion
        try {
            for (int i = 0; i < handler.getBubbles().size(); i += 2) {
                chatBubblesVBox.getChildren().add(getHBox("right", handler.getBubbles().get(i)));
                chatBubblesVBox.getChildren().add(getHBox("left", handler.getBubbles().get(i + 1)));
            }
        } catch (Exception e) {
            alertHandler.alertFailure("Your data wasn't processed correctly.");
        }

        chatBubblesVBox.setMinWidth(ScreenSizehandler.getWidth() * 0.73);

        chatBubbles.setContent(chatBubblesVBox);

        VBox meetingDetails = new VBox();
        meetingDetails.setMaxWidth(325);
        meetingDetails.setMinWidth(325);
        meetingDetails.setSpacing(1);
        meetingDetails.setMinHeight(ScreenSizehandler.getHeight()); // to have vertical separator (which is a VBox
                                                                    // border) span entire screen.

        Label meetingInformation = new Label("Meeting Information\n");
        meetingInformation.setTextAlignment(TextAlignment.CENTER);
        meetingInformation.getStyleClass().add("bigTitle");

        Separator bigSep = new Separator(Orientation.HORIZONTAL);
        bigSep.setId("bigSep");

        Label meetingName = new Label(
                "Meeting Name: " + handler.getStreamTranscript().getName().replace("_AutoGeneratedCaption.vtt", ""));
        meetingName.setWrapText(true);

        Label meetingSpeakers = new Label(
                "Participants: " + handler.getSpeakers());
        meetingSpeakers.setWrapText(true);

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
                "Words spoken per minute: "
                        + Math.round(handler.getNumberOfWords() / (handler.getMeetingDurationSeconds() / 60)));

        if (handler.getNumberOfWords() == 0 || handler.getMeetingDurationSeconds() == 0
                || (handler.getNumberOfWords() / (handler.getMeetingDurationSeconds() / 60)) > 500) {
            wordsPerMinute.setText("Words spoken per minute: not found.");
        }

        Label avgSentenceLength = new Label(
                "Average sentence length: " + handler.getNumberOfWords() / handler.getFullSentenceCount() + " words");
        if (handler.getNumberOfWords() == 0 || handler.getFullSentenceCount() == 0) {
            avgSentenceLength.setText("Average sentence length: not found.");
        }

        // add everything to the view, in a VBox to the right.
        meetingDetails.getChildren().addAll(meetingInformation, bigSep, meetingName, getSep(), meetingSpeakers,
                getSep(), meetingLanguage,
                getSep(), meetingDuration,
                getSep(), totalSeconds, getSep(), meetingRecog, getSep(),
                wordCount, getSep(), wordsPerMinute, getSep(), sentenceCount, getSep(), avgSentenceLength);
        meetingDetails.getStyleClass().add("transcriptVBox");
        meetingDetails.getChildren().forEach(c -> {
            if (c != meetingInformation) {
                c.getStyleClass().add("meetingInfo");
            }
        });

        mainPane.setCenter(chatBubbles);
        mainPane.setRight(meetingDetails);

    }

    /**
     * a method that creates a vertical separator to add to the meeting info side
     * panel.
     * 
     * @return Separator object created and styled.
     */
    public Separator getSep() {
        Separator smallSep = new Separator(Orientation.HORIZONTAL);
        smallSep.setId("smallSep");
        return smallSep;
    }

    public BorderPane getView() {
        return mainPane;
    }

}
