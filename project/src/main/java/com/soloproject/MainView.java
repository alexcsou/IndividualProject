package com.soloproject;

import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainView {

    private BorderPane mainPane;

    public MainView() {
        mainPane = new BorderPane();
        mainPane.getStyleClass().addAll("mainView");
        makeView();
    }

    public void makeView() {

        TabPane tabs = new TabPane();

        Tab dashboard = new Tab("Dashboard", new Label("content"));
        Tab charts = new Tab("Charts", new Label("content2"));
        Tab insights = new Tab("Insights", new Label("content3"));
        Tab transcript = new Tab("Transcript", new Label("content4"));
        Tab settings = new Tab("Settings", new Label("content5"));

        tabs.getTabs().addAll(dashboard, charts, insights, transcript, settings);
        tabs.getTabs().forEach(t -> t.setClosable(false)); // unclosable tabs

        mainPane.setTop(tabs);

    }

    public BorderPane getView() {
        return mainPane;
    }
}
