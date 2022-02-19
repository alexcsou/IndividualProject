package com.soloproject;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * A class that displays a line of text from the transcript as a standalone
 * visual entity. Used in transcriptView.
 */
public class TranscriptBubble {

    private TranscriptSentence sentence;
    private boolean style; // used to determine whether the bubble will be left or right aligned

    public TranscriptBubble(TranscriptSentence sentence, boolean style) {
        this.sentence = sentence;
        this.style = style;
    }

    public BorderPane getBubble() {
        BorderPane bubble = new BorderPane();

        Label sentenceLabel = new Label(sentence.getSentence() + "\n\n");
        sentenceLabel.setMaxWidth(400);
        sentenceLabel.setMinWidth(100);
        sentenceLabel.setWrapText(true);
        sentenceLabel.setId("sentenceLabel");

        Label authorLabel = new Label(sentence.getAuthor() + ":\n\n\n");
        authorLabel.setId("authorLabel");

        Label durationLabel = new Label(" Speech Duration: " + sentence.getDuration() + " seconds"); // cast to string
        Label startAndEndLabel = new Label(sentence.getdurationString());

        HBox durationHBox = new HBox(durationLabel, startAndEndLabel);
        durationHBox.setSpacing(30);
        durationHBox.setId("durationLabels");

        bubble.setTop(authorLabel);
        bubble.setCenter(sentenceLabel);
        bubble.setBottom(durationHBox);

        if (style) {
            bubble.getStyleClass().add("rightChatBubble");
        } else {
            bubble.getStyleClass().add("leftChatBubble");

        }

        return bubble;
    }

    public boolean getStyle() {
        return this.style;
    }
}
