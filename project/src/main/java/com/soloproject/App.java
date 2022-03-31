package com.soloproject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * The main class of the application. It contains the start method and the main
 * method which calls launch(args).
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {

        // this handler creates a MainView. This is because the "choose file" button is
        // the entry point to the application and to the main view, and it is contained
        // in the TranscriptHandler class

        TranscriptHandler handler = new TranscriptHandler(stage);
        WelcomeView welcomeView = new WelcomeView();
        HelpView helpView = new HelpView();

        BorderPane root = new BorderPane();
        root.setTop(welcomeView.getView()); // display the welcome messages

        HBox buttonsRow1 = new HBox();
        buttonsRow1.setSpacing(5);
        buttonsRow1.setAlignment(Pos.CENTER);

        HBox buttonsRow2 = new HBox();
        buttonsRow2.setSpacing(5);
        buttonsRow2.setAlignment(Pos.CENTER);

        VBox buttons = new VBox();
        buttons.getChildren().addAll(buttonsRow1, buttonsRow2);
        buttons.setSpacing(15);
        buttons.setAlignment(Pos.CENTER);

        Button restart = new Button("Reset");
        restart.setOnAction(e -> relaunch(stage));

        // add all buttons to the box
        buttonsRow1.getChildren().addAll(handler.getTeamsButton(), handler.getStreamButton());
        buttonsRow2.getChildren().addAll(helpView.getButton(), handler.getContinueButton(), restart);

        // style three buttons differently from all others manually here.
        helpView.getButton().setId("helpButton");
        handler.getContinueButton().setId("proceedButton");
        restart.setId("resetButton");

        root.setCenter(buttons); // add HBox to the root pane

        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("styling/main.css").toExternalForm());

        stage.setTitle("𝗙𝗜𝗫𝗔𝗧𝗘𝗗");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.png"))); // app icon, show in window
                                                                                            // corner and taskbar.
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // removes hint message for escape and disables
                                                                        // keybind to preserve dimensions
        stage.show();
    }

    /**
     * A method to allows a user to restart Fixated if they have made errors in
     * choosing files.
     * 
     * @param stage the main stage of the application.
     */
    public void relaunch(Stage stage) {
        stage.close();
        start(stage);
    }

    /**
     * Main Method of the application
     * 
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

}
