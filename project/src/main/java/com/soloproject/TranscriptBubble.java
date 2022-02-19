package com.soloproject;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;

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
        sentenceLabel.setWrapText(true);
        Label authorLabel = new Label(sentence.getAuthor());
        Label durationLabel = new Label(" Speech Duration: " + sentence.getDuration()); // cast to string
        Label startAndEndLabel = new Label(sentence.getdurationString());
        startAndEndLabel.setTextAlignment(TextAlignment.RIGHT);
        bubble.setTop(sentenceLabel);
        bubble.setBottom(startAndEndLabel);
        bubble.setLeft(authorLabel);
        bubble.setRight(durationLabel);

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
