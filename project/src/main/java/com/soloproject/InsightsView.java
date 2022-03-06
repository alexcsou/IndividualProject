package com.soloproject;

import javax.servlet.http.Part;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
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
        currentParticipant = handler.getParticipants().get(0); // initialise with first person to speak
        makeView();
    }

    public void makeView() {
        VBox Col1 = new VBox(new Label("1"));
        VBox Col2 = new VBox(new Label("2"));
        VBox Col3 = new VBox(new Label("3"));

        Col1.setMinWidth(ScreenSizehandler.getWidth() * 0.33);
        Col1.setMinHeight(ScreenSizehandler.getHeight() * 0.9);

        Col2.setMinWidth(ScreenSizehandler.getWidth() * 0.33);
        Col2.setMinHeight(ScreenSizehandler.getHeight() * 0.9);

        Col3.setMinWidth(ScreenSizehandler.getWidth() * 0.33);
        Col3.setMinHeight(ScreenSizehandler.getHeight() * 0.9);

        HBox columnContainer = new HBox();
        columnContainer.getChildren().addAll(Col1, Col2, Col3);

        mainPane.setCenter(columnContainer);

        ComboBox<Participant> ParticipantsBox = new ComboBox<>();

        for (Participant p : handler.getParticipants()) {
            ParticipantsBox.getItems().add(p);
        }

        ParticipantsBox.getSelectionModel().selectFirst();
        ParticipantsBox.setOnAction(e -> setCurrentParticipant(ParticipantsBox.getSelectionModel().getSelectedItem()));

        mainPane.setTop(ParticipantsBox);
    }

    public void setCurrentParticipant(Participant p) {
        this.currentParticipant = p;
    }

    public BorderPane getView() {
        return mainPane;
    }
}