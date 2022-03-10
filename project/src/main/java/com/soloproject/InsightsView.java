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

/**
 * The InsightsView class is a view shown under the "insights" tab of the app.
 * It contains three columns with insights linked together horizontally. Each of
 * these is an "InsightBubble". The first column is for what advice is given to
 * a participant, the second for the effect the advice will have, and the third
 * for the way the issue was noticed. Everything displayed is dependent on the
 * currently selected Participant in a drop down box.
 */
public class InsightsView {

    private TranscriptHandler handler;
    private BorderPane mainPane;
    private Participant currentParticipant; // store current participant and build cols relative to it. Upon selecting
                                            // of another participant in the combobox, refresh and rebuild based on the
                                            // newly selected one

    // each column as a Vbox (insights, how it'll help, and why we noticed it)
    private VBox col1 = new VBox();
    private VBox col2 = new VBox();
    private VBox col3 = new VBox();

    public InsightsView(TranscriptHandler handler) {
        this.handler = handler; // pass the handler to create relevant insight bubbles using its data.
        mainPane = new BorderPane();
        mainPane.getStyleClass().add("insightsView");
        currentParticipant = handler.getParticipants().get(0); // initialise with first person to speak
        makeView();
    }

    public void makeView() {

        // style cols
        col1.getStyleClass().add("insightsView");
        col2.getStyleClass().add("insightsView");
        col3.getStyleClass().add("insightsView");

        // size cols
        col1.setMinWidth(ScreenSizehandler.getWidth() * 0.3);
        col1.setMinHeight(ScreenSizehandler.getHeight() * 0.92);

        col2.setMinWidth(ScreenSizehandler.getWidth() * 0.3);
        col2.setMinHeight(ScreenSizehandler.getHeight() * 0.92);

        col3.setMinWidth(ScreenSizehandler.getWidth() * 0.3);
        col3.setMinHeight(ScreenSizehandler.getHeight() * 0.92);

        // All cols in an HBox
        HBox columnContainer = new HBox();
        columnContainer.getChildren().addAll(col1, getSep(Orientation.VERTICAL), col2, getSep(Orientation.VERTICAL),
                col3);

        // Cols HBox at center of a borderpane
        mainPane.setCenter(columnContainer);

        // Combobox for selecting user at top of borderpane
        ComboBox<Participant> ParticipantsBox = getComboBox();

        Label infoText = new Label("Select Participant: ");
        HBox topComponents = new HBox(infoText, ParticipantsBox);
        mainPane.setTop(topComponents);
    }

    /**
     * A setter method for the current participant
     * 
     * @param p Participant to set as current participant. Fetched from combox.
     */
    public void setCurrentParticipant(Participant p) {
        this.currentParticipant = p;
    }

    /**
     * Method to wrap the mainPane borderpane in a scrollpane and return it. Used
     * and called in mainview.
     * 
     * @return a scrollpane containing everything else in insightsview.
     */
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

    /**
     * A method to create and return a comboxbox with all participant objects. Upon
     * selecting a participant, the view is refreshed and new insights are built.
     * 
     * @return the comboBox containing participants.
     */
    public ComboBox<Participant> getComboBox() {
        ComboBox<Participant> ParticipantsBox = new ComboBox<>();

        for (Participant p : handler.getParticipants()) {
            ParticipantsBox.getItems().add(p);
        }

        ParticipantsBox.setOnAction(e -> refresh(ParticipantsBox.getSelectionModel().getSelectedItem()));

        ParticipantsBox.getStyleClass().add("participantBox");

        return ParticipantsBox;
    }

    /**
     * A method called upon chosing a new participant from the combobox. Refreshes
     * the view and rebuilds the columns
     * 
     * @param p the new participant to build for.
     */
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

    /**
     * Build the first column. Style it and clear it first before re adding the
     * column title and separators as this is called whenever the view is refreshed
     */
    public void addCol1Insights() {
        Label col1Label = new Label("How can I improve?");
        col1Label.getStyleClass().add("insightLabel");

        col1.getChildren().clear();

        col1.getChildren().add(col1Label);
        col1.getChildren().add(getSep(Orientation.HORIZONTAL));

        col1.getChildren().add(new InsightBubble(col1Bubble1()).getBubble());
    }

    /**
     * Build the second column. Style it and clear it first before re adding the
     * column title and separators as this is called whenever the view is refreshed
     */
    public void addCol2Insights() {
        Label col2Label = new Label("How will it help?");
        col2Label.getStyleClass().add("insightLabel");

        col2.getChildren().clear();

        col2.getChildren().add(col2Label);
        col2.getChildren().add(getSep(Orientation.HORIZONTAL));
        col2.getChildren().add(new InsightBubble(col2Bubble1()).getBubble());
    }

    /**
     * Build the third column. Style it and clear it first before re adding the
     * column title and separators as this is called whenever the view is refreshed
     */
    public void addCol3Insights() {
        Label col3Label = new Label("How did the issue get noticed?");
        col3Label.getStyleClass().add("insightLabel");

        col3.getChildren().clear();

        col3.getChildren().add(col3Label);
        col3.getChildren().add(getSep(Orientation.HORIZONTAL));
        col3.getChildren().add(new InsightBubble(col3Bubble1()).getBubble());
    }

    /**
     * Returns a bubble that advises a user on how to improve the speed of their
     * speech. based off of
     * https://cloudflare-ipfs.com/ipfs/QmXoypizjW3WknFiJnKLwHCnL72vedxjQkDDP1mXWo6uco/wiki/Words_per_minute.html
     * 
     * @return string the string to display in the bubble.
     */
    public String col1Bubble1() {
        if (currentParticipant.getWpm(handler) < 110) {
            return "You spoke at a considerably slower speed than average. Try to speak a little faster.";
        } else if (currentParticipant.getWpm(handler) >= 110 && currentParticipant.getWpm(handler) < 170) {
            return "You spoke at a very understandable speed. Keep it up!";
        } else if (currentParticipant.getWpm(handler) >= 170 && currentParticipant.getWpm(handler) < 220) {
            return "You spoke at a considerably faster speed than average. Try to speak a little slower"
                    + " and to take the time better articulate your words.";
        } else {
            return "You spoke at very high speed. Try to greatly reduce the speed at which you speak.";
        }
    }

    /**
     * Returns a bubble that advises a user on how the effect of improving their
     * speech speed will change a conversation.
     * based off of
     * https://cloudflare-ipfs.com/ipfs/QmXoypizjW3WknFiJnKLwHCnL72vedxjQkDDP1mXWo6uco/wiki/Words_per_minute.html
     * 
     * @return string the string to display in the bubble.
     */
    public String col2Bubble1() {
        return "Having a controlled and correctly paced speaking speed will let you feel more relaxed and in control of your"
                + " discourse. It'll also give your words more importance and let your audience better understand and follow"
                + " your train of thought. It is also likely to regulate your breathing and allow you to better control the "
                + "emotions you insert in your discourse.";
    }

    /**
     * Returns a bubble that advises a user on their speech speed compared to the
     * norm.
     * From:
     * https://cloudflare-ipfs.com/ipfs/QmXoypizjW3WknFiJnKLwHCnL72vedxjQkDDP1mXWo6uco/wiki/Words_per_minute.html
     * 
     * @return string the string to display in the bubble.
     */
    public String col3Bubble1() {
        return "Your word per minute speech speed (wpm) during this meeting was: "
                + Math.round(currentParticipant.getWpm(handler))
                + ". A typical conversational speed is between 110 and 170 words per minute. Extremely fast speakers will reach 220+"
                + " words per minute.";
    }
}