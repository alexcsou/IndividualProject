package com.soloproject;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * The InsightBubble class creates a visual element containing simple provided
 * text, normally supposed to be dependent on the currently selected participant
 * in the insights tab.
 */
public class InsightBubble {

    private String content;
    private String title;

    private BorderPane mainPane;

    public InsightBubble(String title, String content) {
        this.content = content;
        this.title = title;
        mainPane = new BorderPane();
        mainPane.getStyleClass().add("insightBubble");
        makeBubble();
    }

    /**
     * A method that builds the Bubble and its content.
     */
    public void makeBubble() {
        Label contentLabel = new Label(content);
        contentLabel.setMaxWidth(ScreenSizehandler.getWidth() * 0.23);
        contentLabel.setMinHeight(ScreenSizehandler.getHeight() * 0.15);
        contentLabel.setWrapText(true);

        Label titleLabel = new Label(title);
        titleLabel.setWrapText(true);
        titleLabel.getStyleClass().addAll("insightsTitle");

        mainPane.setCenter(contentLabel);
        mainPane.setTop(new VBox(titleLabel, getSep(Orientation.HORIZONTAL)));
    }

    /**
     * Return the BorderPane in which the bubble is built
     * 
     * @return BorderPane the bubble
     */
    public BorderPane getBubble() {
        return mainPane;
    }

    /**
     * A method that creates a separator with a provided orientation. used to create
     * large amounts of separators for laying out the getKPIs() grid.
     * 
     * @param or the orientation of the separator
     * @return a separator with the provided orientation.
     */
    public Separator getSep(Orientation or) {
        Separator smallSep = new Separator(or);
        smallSep.setId("insightBubbleSep");
        smallSep.setMaxWidth(ScreenSizehandler.getWidth() * 0.20);
        return smallSep;
    }
}
