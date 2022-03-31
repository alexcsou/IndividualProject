package com.soloproject;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The mainView of the application, which contains all tabs and appears
 * fullscreened upon first launch.
 * This is instantiated in TranscriptHandler upon closing the file process
 * success alert.
 */
public class MainView {

    private BorderPane mainPane;
    private Stage stage;
    private TranscriptHandler handler; // the mainView has a handler as a way to access meeting transcript info
    private TranscriptView transcriptView; // This view is passed the transcript handler as to display it's data.
    private DashboardView dashboardView;
    private InsightsView insightView;

    public MainView(Stage stage, TranscriptHandler handler) {

        this.handler = handler;

        // create all views
        transcriptView = new TranscriptView(this.handler);
        dashboardView = new DashboardView(this.handler);
        insightView = new InsightsView(this.handler);

        mainPane = new BorderPane();
        mainPane.getStyleClass().addAll("mainView");

        this.stage = stage;
        stage.setMinHeight(ScreenSizeHandler.getHeight());
        stage.setMinWidth(ScreenSizeHandler.getWidth());

        makeView();
    }

    public void makeView() {

        TabPane tabs = new TabPane();

        // Each tab is a different class of view.
        Tab dashboard = new Tab("Dashboard", new StackPane(dashboardView.getView()));
        Tab insights = new Tab("Insights", insightView.getView());
        Tab transcript = new Tab("Transcript", transcriptView.getView());

        tabs.getTabs().addAll(dashboard, insights, transcript);
        tabs.getTabs().forEach(t -> t.setClosable(false)); // unclosable tabs

        Button quitButton = new Button("Quit"); // extra quit button as user is fullscreened and should be able to quit
                                                // at any time.
        quitButton.setOnAction(e -> stage.close());
        quitButton.getStyleClass().add("quitButton");

        // add the tabs and quit button to the top of the mainpane.
        HBox topComponents = new HBox(tabs, quitButton);
        HBox.setHgrow(tabs, Priority.ALWAYS);

        mainPane.setTop(topComponents);

    }

    /**
     * Returns the view
     * 
     * @return BorderPane the view.
     */
    public BorderPane getView() {
        return mainPane;
    }

}
