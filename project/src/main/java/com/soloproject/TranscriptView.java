package com.soloproject;

import javafx.scene.layout.BorderPane;

public class TranscriptView {

    private BorderPane mainPane;
    private TranscriptHandler handler;

    public TranscriptView(TranscriptHandler handler) {
        this.handler = handler;
        mainPane = new BorderPane();
        makeView();
    }

    public void makeView() {
        System.out.println("duration was: " + handler.getMeetingDurationString());
    }

    public BorderPane getView() {
        return mainPane;
    }

}
