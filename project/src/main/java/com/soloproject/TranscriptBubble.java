package com.soloproject;

import javafx.scene.control.Label;

/**
 * A class that displays a line of text from the transcript as a standalone
 * visual entity. Used in transcriptView.
 */
public class TranscriptBubble {

    private TranscriptSentence sentence;

    public TranscriptBubble(TranscriptSentence sentence) {
        this.sentence = sentence;
    }

    public Label getBubble() {
        Label label = new Label(sentence.getSentence());
        label.getStyleClass().add("chatBubble");
        return label;
    }
}
