package com.soloproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        VideoInput input = new VideoInput(stage);
        BorderPane root = new BorderPane();
        root.setCenter(input.getButton());
        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Fixated");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}