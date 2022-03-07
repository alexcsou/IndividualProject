package com.soloproject;

import javax.servlet.http.Part;

import javafx.geometry.Orientation;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class InsightsView {

    private TranscriptHandler handler;
    private BorderPane mainPane;
    private Participant currentParticipant;

    public InsightsView(TranscriptHandler handler) {
        this.handler = handler;
        mainPane = new BorderPane();
        mainPane.getStyleClass().add("insightsView");
        currentParticipant = handler.getParticipants().get(0); // initialise with first person to speak
        makeView();
    }

    public void makeView() {
        Label col1Label = new Label("How can I improve?");
        col1Label.getStyleClass().add("insightLabel");
        VBox Col1 = new VBox(col1Label);
        Col1.getStyleClass().add("insightsView");

        addCol1Insights(Col1);

        Label col2Label = new Label("How will it help?");
        col2Label.getStyleClass().add("insightLabel");
        VBox Col2 = new VBox(col2Label);
        Col2.getStyleClass().add("insightsView");

        Label col3Label = new Label("How did the issue get noticed?");
        col3Label.getStyleClass().add("insightLabel");
        VBox Col3 = new VBox(col3Label);
        Col3.getStyleClass().add("insightsView");

        Col1.setMinWidth(ScreenSizehandler.getWidth() * 0.3);
        Col1.setMinHeight(ScreenSizehandler.getHeight() * 0.92);

        Col2.setMinWidth(ScreenSizehandler.getWidth() * 0.3);
        Col2.setMinHeight(ScreenSizehandler.getHeight() * 0.92);

        Col3.setMinWidth(ScreenSizehandler.getWidth() * 0.3);
        Col3.setMinHeight(ScreenSizehandler.getHeight() * 0.92);

        HBox columnContainer = new HBox();
        columnContainer.getChildren().addAll(Col1, getSep(Orientation.VERTICAL), Col2, getSep(Orientation.VERTICAL),
                Col3);

        mainPane.setCenter(columnContainer);

        ComboBox<Participant> ParticipantsBox = getComboBox();

        mainPane.setTop(ParticipantsBox);
    }

    public void setCurrentParticipant(Participant p) {
        this.currentParticipant = p;
    }

    public ScrollPane getView() {

        ScrollPane returnPane = new ScrollPane();
        returnPane.getStyleClass().add("scrollPane");
        returnPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        returnPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        returnPane.setContent(mainPane);
        returnPane.setMaxHeight(ScreenSizehandler.getHeight() * 0.92);
        returnPane.setMaxWidth(ScreenSizehandler.getWidth() * 1);
        return returnPane;
    }

    public ComboBox<Participant> getComboBox() {
        ComboBox<Participant> ParticipantsBox = new ComboBox<>();

        for (Participant p : handler.getParticipants()) {
            ParticipantsBox.getItems().add(p);
        }

        ParticipantsBox.getSelectionModel().selectFirst();
        ParticipantsBox.setOnAction(e -> setCurrentParticipant(ParticipantsBox.getSelectionModel().getSelectedItem()));

        ParticipantsBox.getStyleClass().add("participantBox");

        return ParticipantsBox;
    }

    /**
     * a method that creates a vertical separator to add to the meeting info side
     * panel.
     * 
     * @return Separator object created and styled.
     */
    public Separator getSep(Orientation or) {
        Separator smallSep = new Separator(or);
        smallSep.setId("bigSep");
        return smallSep;
    }

    public void addCol1Insights(VBox col) {
        col.getChildren().add(new InsightBubble("this is a test", currentParticipant).getBubble());
    }
}