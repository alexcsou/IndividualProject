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

    private VBox col1 = new VBox();
    private VBox col2 = new VBox();
    private VBox col3 = new VBox();

    public InsightsView(TranscriptHandler handler) {
        this.handler = handler;
        mainPane = new BorderPane();
        mainPane.getStyleClass().add("insightsView");
        currentParticipant = handler.getParticipants().get(0); // initialise with first person to speak

        makeView();
    }

    public void makeView() {

        col1.getStyleClass().add("insightsView");
        col2.getStyleClass().add("insightsView");
        col3.getStyleClass().add("insightsView");

        addCol1Insights();
        addCol2Insights();
        addCol3Insights();

        col1.setMinWidth(ScreenSizehandler.getWidth() * 0.3);
        col1.setMinHeight(ScreenSizehandler.getHeight() * 0.92);

        col2.setMinWidth(ScreenSizehandler.getWidth() * 0.3);
        col2.setMinHeight(ScreenSizehandler.getHeight() * 0.92);

        col3.setMinWidth(ScreenSizehandler.getWidth() * 0.3);
        col3.setMinHeight(ScreenSizehandler.getHeight() * 0.92);

        HBox columnContainer = new HBox();
        columnContainer.getChildren().addAll(col1, getSep(Orientation.VERTICAL), col2, getSep(Orientation.VERTICAL),
                col3);

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
        ParticipantsBox.setOnAction(e -> refresh(ParticipantsBox.getSelectionModel().getSelectedItem()));

        ParticipantsBox.getStyleClass().add("participantBox");

        return ParticipantsBox;
    }

    public void refresh(Participant p) {
        setCurrentParticipant(p);
        addCol1Insights();
        addCol2Insights();
        addCol3Insights();
    }

    /**
     * a method that creates a vertical separator to add to the meeting info side
     * panel.
     * 
     * @return Separator object created and styled.
     */
    public Separator getSep(Orientation or) {
        Separator smallSep = new Separator(or);
        smallSep.setId("InsightSep");
        return smallSep;
    }

    public void addCol1Insights() {
        Label col1Label = new Label("How can I improve?");
        col1Label.getStyleClass().add("insightLabel");

        col1.getChildren().clear();

        col1.getChildren().add(col1Label);
        col1.getChildren().add(getSep(Orientation.HORIZONTAL));
        col1.getChildren().add(new InsightBubble("this is a test", currentParticipant).getBubble());
    }

    public void addCol2Insights() {
        Label col2Label = new Label("How will it help?");
        col2Label.getStyleClass().add("insightLabel");

        col2.getChildren().clear();

        col2.getChildren().add(col2Label);
        col2.getChildren().add(getSep(Orientation.HORIZONTAL));
        col2.getChildren().add(new InsightBubble("this is a test 2", currentParticipant).getBubble());
    }

    public void addCol3Insights() {
        Label col3Label = new Label("How did the issue get noticed?");
        col3Label.getStyleClass().add("insightLabel");

        col3.getChildren().clear();

        col3.getChildren().add(col3Label);
        col3.getChildren().add(getSep(Orientation.HORIZONTAL));
        col3.getChildren().add(new InsightBubble("this is a test 3", currentParticipant).getBubble());
    }
}