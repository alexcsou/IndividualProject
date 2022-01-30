package com.soloproject;

import java.io.File;

import org.openimaj.image.MBFImage;
import org.openimaj.video.VideoDisplay;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.InputStream;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.result.WordResult;

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
        try {
            getSpeech(video);
        } catch (Exception e) {
            System.out.println("something went wrong with the video");
        }

    }

    public Button getButton() {
        return button;
    }

    public String getSpeech(File video) throws Exception {

        InputStream videostream = new FileInputStream(video);

        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);

        recognizer.startRecognition(videostream);
        recognizer.stopRecognition();
        SpeechResult result = recognizer.getResult();
        System.out.println(result.getHypothesis());
        return result.getHypothesis();
    }
}
