package com.soloproject;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * A view that a user is first presented with upon launching Fixated. It
 * contains text giving some basic info about the app.
 */
public class WelcomeView {

    private VBox mainbox = new VBox();
    private Image logo = (new Image(getClass().getResourceAsStream("images/logo.png")));

    public WelcomeView() {
        mainbox.getStyleClass().add("welcomeView");
        mainbox.setAlignment(Pos.CENTER);
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
                "(If you aren't sure how to acquire a transcript file, click the help button below.)");
        infoText1.setWrapText(true);
        infoText1.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(titleLabel, infoText1); // both items in a VBox
    }

    public VBox getView() {
        return mainbox;
    }
}
