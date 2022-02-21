package com.soloproject;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * The mainView of the application, which contains all tabs and appears
 * fullscreened upon first launch.
 * This is isntantiated in TranscriptHandler upon closing the file process
 * success alert.
 */
public class MainView {

    private BorderPane mainPane;
    private Stage stage;
    private TranscriptHandler handler; // the mainView has a handler as a way to access meeting transcript info
    private TranscriptView transcriptView; // This view is passed the transcript handler as to display it's data.
    private DashboardView dashboardView;

    public MainView(Stage stage, TranscriptHandler handler) {
        this.handler = handler;

        transcriptView = new TranscriptView(this.handler);
        dashboardView = new DashboardView();

        mainPane = new BorderPane();
        mainPane.getStyleClass().addAll("mainView");
        this.stage = stage;
        stage.setMinHeight(ScreenSizehandler.getHeight());
        stage.setMinWidth(ScreenSizehandler.getWidth());

        makeView();
    }

    public void makeView() {

        TabPane tabs = new TabPane();

        // Each tab is a different class of view.
        Tab dashboard = new Tab("Dashboard", dashboardView.getView());
        Tab charts = new Tab("Charts", new Label("content2"));
        Tab insights = new Tab("Insights", new Label("content3"));
        Tab transcript = new Tab("Transcript", transcriptView.getView());
        Tab settings = new Tab("Settings", new Label("content5"));

        tabs.getTabs().addAll(dashboard, charts, insights, transcript, settings);
        tabs.getTabs().forEach(t -> t.setClosable(false)); // unclosable tabs

        Button quitButton = new Button("Quit"); // extra quit button as user is fullscreened and should be able to quit
                                                // at any time.
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
