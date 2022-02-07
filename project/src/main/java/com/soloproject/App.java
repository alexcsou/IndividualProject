package com.soloproject;

import javafx.application.Application;
import javafx.css.StyleClass;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;

/**
 * The main class of the application. It contains the start method and the main
 * method which calls launch(args).
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {

        TranscriptHandler handler = new TranscriptHandler(stage);
        WelcomeView welcomeView = new WelcomeView();

        BorderPane root = new BorderPane();
        root.setTop(welcomeView.getView()); // display the welcome messages
        handler.getButton().setDefaultButton(true); // if a user presses enter, file select screen opens

        HBox buttons = new HBox();
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(handler.getButton(), welcomeView.getHelpButton()); // add both buttons to the HBox

        root.setCenter(buttons); // add HBox to the root pane

        Scene scene = new Scene(root, 700, 500);
        scene.getStylesheets().add(getClass().getResource("styling/main.css").toExternalForm());

        stage.setTitle("Fixated");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
