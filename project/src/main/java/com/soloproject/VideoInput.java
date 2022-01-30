package com.soloproject;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * A class to allow a user to select the video file they want analysed by
 * Fixated.
 */
public class VideoInput {

    private Button button = new Button("Choose a file");
    private Stage stage;

    public VideoInput(Stage stage) {
        button.setOnAction(e -> openChooser());
        this.stage = stage;
    }

    public void openChooser() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a Meeting recording");
        File video = chooser.showOpenDialog(stage);
        if (video != null) {

        }
    }

    public Button getButton() {
        return button;
    }
}