package com.soloproject;

import java.io.FileInputStream;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;

/**
 * A view that a user is first presented with upon launching Fixated. It
 * contains text giving some basic info about the app.
 */
public class WelcomeView {

    private VBox mainbox = new VBox();
    private Image logo = (new Image(getClass().getResourceAsStream("images/logo.png")));
    private Button HelpButton;

    public WelcomeView() {
        mainbox.getStyleClass().add("welcomeView");
        mainbox.setAlignment(Pos.CENTER);
        HelpButton = new Button("Help");
        HelpButton.setOnAction(e -> showHelp());
        makeView(mainbox);
    }

    public void makeView(VBox box) {

        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(60);
        logoView.setFitWidth(60);

        Label titleLabel = new Label("𝗙𝗜𝗫𝗔𝗧𝗘𝗗", logoView); // fancy case title
        titleLabel.getStyleClass().add("titleLabel");

        Label infoText1 = new Label("Welcome to 𝗙𝗜𝗫𝗔𝗧𝗘𝗗! Please start by selecting " +
                "a WebTTV transcript file (.vtt) of your meeting. " +
                "If you aren't sure how to acquire a transcript file, click the help button below.");
        infoText1.setWrapText(true);
        infoText1.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleLabel, infoText1); // both items in a VBox
    }

    public void showHelp() {
        HelpButton.setDisable(true);
        BorderPane helpPane = new BorderPane();
        Scene scene = new Scene(helpPane, ScreenSizehandler.getWidth() * 0.45, ScreenSizehandler.getHeight() * 0.75);
        scene.getStylesheets().add(getClass().getResource("styling/main.css").toExternalForm());
        Stage helpWindow = new Stage();
        helpWindow.setTitle(".vtt Import help");

        helpWindow.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.png")));
        helpWindow.setScene(scene);
        helpWindow.showAndWait();// disable help button until user closes help window
        HelpButton.setDisable(false);

    }

    public VBox getView() {
        return mainbox;
    }

    public Button getHelpButton() {
        return HelpButton;
    }
}
