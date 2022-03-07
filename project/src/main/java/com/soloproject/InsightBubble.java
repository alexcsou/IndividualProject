package com.soloproject;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class InsightBubble {

    private String content;
    private Participant participant;
    private BorderPane mainPane;

    public InsightBubble(String content, Participant participant) {
        this.content = content;
        this.participant = participant;
        mainPane = new BorderPane();
        mainPane.getStyleClass().add("insightBubble");
        makeBubble();
    }

    public void makeBubble() {
        mainPane.setCenter(new Label(content));
    }

    public BorderPane getBubble() {
        return mainPane;
    }
}
