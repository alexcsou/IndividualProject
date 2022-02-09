package com.soloproject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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
        helpView helpView = new helpView();

        BorderPane root = new BorderPane();
        root.setTop(welcomeView.getView()); // display the welcome messages
        handler.getButton().setDefaultButton(true); // if a user presses enter, file select screen opens

        HBox buttons = new HBox();
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(handler.getButton(), helpView.getButton()); // add both buttons to the HBox

        root.setCenter(buttons); // add HBox to the root pane

        Scene scene = new Scene(root, ScreenSizehandler.getWidth() / 2, // size relatively to screen size
                ScreenSizehandler.getHeight() / 2);

        scene.getStylesheets().add(getClass().getResource("styling/main.css").toExternalForm());

        stage.setTitle("𝗙𝗜𝗫𝗔𝗧𝗘𝗗");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
