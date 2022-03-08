package com.soloproject;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * The InsightBubble class creates a visual element containing simple provided
 * text, normally supposed to be dependent on the currently selected participant
 * in the insights tab.
 */
public class InsightBubble {

    private String content;

    private BorderPane mainPane;

    public InsightBubble(String content, Participant participant) {
        this.content = content;
        mainPane = new BorderPane();
        mainPane.getStyleClass().add("insightBubble");
        makeBubble();
    }

    /**
     * A method that builds the Bubble and its content.
     */
    public void makeBubble() {
        mainPane.setCenter(new Label(content));
    }

    /**
     * Return the BorderPane in which the bubble is built
     * 
     * @return BorderPane the bubble
     */
    public BorderPane getBubble() {
        return mainPane;
    }
}
