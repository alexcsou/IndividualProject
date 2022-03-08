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

    /**
     * Method that builds the welcome view.
     * 
     * @param box the VBox to build the view items into, here the field mainbox
     */
    public void makeView(VBox box) {

        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(60);
        logoView.setFitWidth(60);

        Label titleLabel = new Label("𝗙𝗜𝗫𝗔𝗧𝗘𝗗", logoView); // fancy case title
        titleLabel.getStyleClass().add("titleLabel");

        Label infoText1 = new Label(
                "Welcome to 𝗙𝗜𝗫𝗔𝗧𝗘𝗗! To start, select and upload both WebTTV transcript files (.vtt)"
                        + " of your meeting: \n\n\n 1 - The first one can be downloaded directly from Microsoft Teams."
                        + "\n 2 - The second should be downloaded from Microsoft Stream. ");

        Label infoText2 = new Label(" \n \n If you aren't sure how to acquire"
                + " either file, click the help button below. \n"
                + " If you make a mistake selecting your files, click the reset button.\n\n\n");
        infoText1.setWrapText(true);
        infoText1.setTextAlignment(TextAlignment.CENTER);
        infoText2.setWrapText(true);
        infoText2.setTextAlignment(TextAlignment.CENTER);
        infoText2.getStyleClass().addAll("italicsLabel");

        box.getChildren().addAll(titleLabel, infoText1, infoText2); // both items in a VBox
    }

    /**
     * getter for the view
     * 
     * @return VBox the welcome view
     */
    public VBox getView() {
        return mainbox;
    }
}
