package com.soloproject;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class MainView {

    private BorderPane mainPane;
    private Stage stage;
    private TranscriptHandler handler;
    private TranscriptView transcriptView;

    public MainView(Stage stage, TranscriptHandler handler) {
        this.handler = handler;
        transcriptView = new TranscriptView(this.handler);
        mainPane = new BorderPane();
        mainPane.getStyleClass().addAll("mainView");
        this.stage = stage;
        makeView();
    }

    public void makeView() {

        TabPane tabs = new TabPane();

        Tab dashboard = new Tab("Dashboard", new Label("content"));
        Tab charts = new Tab("Charts", new Label("content2"));
        Tab insights = new Tab("Insights", new Label("content3"));
        Tab transcript = new Tab("Transcript", transcriptView.getView());
        Tab settings = new Tab("Settings", new Label("content5"));

        tabs.getTabs().addAll(dashboard, charts, insights, transcript, settings);
        tabs.getTabs().forEach(t -> t.setClosable(false)); // unclosable tabs

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> stage.close());
        quitButton.getStyleClass().add("quitButton");

        HBox topComponents = new HBox(tabs, quitButton);
        HBox.setHgrow(tabs, Priority.ALWAYS);

        mainPane.setTop(topComponents);

    }

    public BorderPane getView() {
        return mainPane;
    }

}
