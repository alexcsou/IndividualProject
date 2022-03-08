package com.soloproject;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    /**
     * The getter method for this bubble, returns the Pane that contains it
     * 
     * @return Borderpane containg the sentence, the author and the duration
     *         timestamp.
     */
    public BorderPane getBubble() {
        BorderPane bubble = new BorderPane();

        Label sentenceLabel = new Label(sentence.getSentence());
        sentenceLabel.setMaxWidth(400);
        sentenceLabel.setMinWidth(100);
        sentenceLabel.setWrapText(true);
        sentenceLabel.setId("sentenceLabel");

        // separators for visual clarity
        Separator smallSep1 = new Separator(Orientation.HORIZONTAL);

        Separator smallSep2 = new Separator(Orientation.HORIZONTAL);

        VBox sentenceVBox = new VBox(smallSep1, sentenceLabel, smallSep2);
        sentenceVBox.setSpacing(10);

        Label authorLabel = new Label(sentence.getAuthor() + ":\n\n\n");
        authorLabel.setId("authorLabel");

        Label durationLabel = new Label("\n\n Speech Duration: " + sentence.getDuration() + " seconds"); // cast to
                                                                                                         // string
        durationLabel.setId("durationLabels");

        Label startAndEndLabel = new Label("\n\n" + sentence.getdurationString());
        startAndEndLabel.setId("durationLabels");

        Label sep = new Label("\n\n  | ");
        sep.setId("durationLabels");

        HBox durationHBox = new HBox(durationLabel, sep, startAndEndLabel);
        durationHBox.setSpacing(30);
        durationHBox.setId("durationLabels");

        bubble.setTop(authorLabel);
        bubble.setCenter(sentenceVBox);
        bubble.setBottom(durationHBox);

        if (style) { // conditional styling for left or right bubbles
            bubble.getStyleClass().add("rightChatBubble");
        } else {
            bubble.getStyleClass().add("leftChatBubble");

        }

        return bubble;
    }

    /**
     * A getter for the style field
     * 
     * @return boolean the field style
     */
    public boolean getStyle() {
        return this.style;
    }
}
