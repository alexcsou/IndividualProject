package com.soloproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

import javafx.scene.media.Media;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        TranscriptHandler handler = new TranscriptHandler(stage);

        BorderPane root = new BorderPane();
        root.setCenter(handler.getFileSelectButton());

        Scene scene = new Scene(root, 640, 480);

        stage.setTitle("Fixated");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
