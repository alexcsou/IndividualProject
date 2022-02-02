package com.soloproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * The main class of the application. It contains the start method and the main
 * method which calls launch(args).
 */
public class App extends Application {

    @Override
    /**
     * @param stage the stage of the GUI
     */
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
