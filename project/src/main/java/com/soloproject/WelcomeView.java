package com.soloproject;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class WelcomeView {

    private VBox mainbox = new VBox();

    public WelcomeView() {
        mainbox.getStyleClass().add("welcomeView");
        mainbox.setAlignment(Pos.CENTER);
        makeView(mainbox);
    }

    public void makeView(VBox box) {
        Label infoText1 = new Label("Welcome to Fixated! Please start by selecting " +
                "a WebTTV transcript file (.vtt) of your meeting. " +
                "If you aren't sure how to do this, click the help button below.");
        infoText1.setWrapText(true);
        infoText1.setTextAlignment(TextAlignment.CENTER);
        ;
        box.getChildren().add(infoText1);
    }

    public VBox getView() {
        return mainbox;
    }

}
