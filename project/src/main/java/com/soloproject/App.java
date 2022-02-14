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

        TranscriptHandler handler = new TranscriptHandler(stage); // this handler creates a MainView. This is because
                                                                  // the "choose file" button is the entry point to the
                                                                  // application and to the main view, and it is
                                                                  // contained in the TranscriptHandler class
        WelcomeView welcomeView = new WelcomeView();
        helpView helpView = new helpView();

        BorderPane root = new BorderPane();
        root.setTop(welcomeView.getView()); // display the welcome messages

        HBox buttons = new HBox();
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.CENTER);
        // add all buttons to the box
        buttons.getChildren().addAll(handler.getStreamButton(), handler.getTeamsButton(), helpView.getButton());

        root.setCenter(buttons); // add HBox to the root pane

        Scene scene = new Scene(root, ScreenSizehandler.getWidth() / 2, // size relatively to screen size
                ScreenSizehandler.getHeight() / 2);

        scene.getStylesheets().add(getClass().getResource("styling/main.css").toExternalForm());

        stage.setTitle("𝗙𝗜𝗫𝗔𝗧𝗘𝗗");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.png"))); // app icon, show in window
                                                                                            // corner and taskbar.
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
