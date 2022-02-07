package com.soloproject;

import javafx.scene.layout.BorderPane;

public class MainView {

    private BorderPane mainPane;

    public MainView() {
        mainPane = new BorderPane();
    }

    public BorderPane getView() {
        return mainPane;
    }
}
