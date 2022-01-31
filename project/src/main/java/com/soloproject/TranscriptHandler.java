package com.soloproject;

import java.io.File;

import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * A class to allow a user to select the video file they want analysed by
 * Fixated.
 */
public class TranscriptHandler {

    private Button button = new Button("Choose a file");
    private Stage stage;
    private File transcript = null;

    public TranscriptHandler(Stage stage) {
        button.setOnAction(e -> chooseFile());
        this.stage = stage;
    }

    public File chooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a WebVTT file (.vtt)");

        chooser.getExtensionFilters().addAll(
                new ExtensionFilter("WebVTT Files", "*.vtt"));

        File video = chooser.showOpenDialog(stage);
        setTranscript(video);
        return video;
    }

    public Button getFileSelectButton() {
        return button;
    }

    public File getTranscript() {
        return transcript;
    }

    public void setTranscript(File file) {
        this.transcript = file;
    }
}
