package com.soloproject;

import javax.servlet.http.Part;

import javafx.geometry.Orientation;
import javafx.scene.Node;
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
        col1.getChildren().add(new InsightBubble(col1Bubble2()).getBubble());
        col1.getChildren().add(new InsightBubble(col1Bubble3()).getBubble());
        col1.getChildren().add(new InsightBubble(col1Bubble4()).getBubble());
        col1.getChildren().add(new InsightBubble(col1Bubble5()).getBubble());

        // color everything except the title and separator
        for (int i = 2; i < col1.getChildren().size(); i++) {
            col1.getChildren().get(i).getStyleClass().add("col1Bubbles");
        }

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
        col2.getChildren().add(new InsightBubble(col2Bubble2()).getBubble());
        col2.getChildren().add(new InsightBubble(col2Bubble3()).getBubble());
        col2.getChildren().add(new InsightBubble(col2Bubble4()).getBubble());
        col2.getChildren().add(new InsightBubble(col2Bubble5()).getBubble());

        // color everything except the title and separator
        for (int i = 2; i < col2.getChildren().size(); i++) {
            col2.getChildren().get(i).getStyleClass().add("col2Bubbles");
        }
    }

    /**
     * Build the third column. Style it and clear it first before re adding the
     * column title and separators as this is called whenever the view is refreshed
     */
    public void addCol3Insights() {
        Label col3Label = new Label("How did this get noticed?");
        col3Label.getStyleClass().add("insightLabel");

        col3.getChildren().clear();

        col3.getChildren().add(col3Label);
        col3.getChildren().add(getSep(Orientation.HORIZONTAL));
        col3.getChildren().add(new InsightBubble(col3Bubble1()).getBubble());
        col3.getChildren().add(new InsightBubble(col3Bubble2()).getBubble());
        col3.getChildren().add(new InsightBubble(col3Bubble3()).getBubble());
        col3.getChildren().add(new InsightBubble(col3Bubble4()).getBubble());
        col3.getChildren().add(new InsightBubble(col3Bubble5()).getBubble());

        // color everything except the title and separator
        for (int i = 2; i < col3.getChildren().size(); i++) {
            col3.getChildren().get(i).getStyleClass().add("col3Bubbles");
        }
    }

    /**
     * Returns a string to put in a bubble that advises a user on how to improve the
     * speed of their
     * speech. based off of
     * https://cloudflare-ipfs.com/ipfs/QmXoypizjW3WknFiJnKLwHCnL72vedxjQkDDP1mXWo6uco/wiki/Words_per_minute.html
     * 
     * @return string the string to display in the bubble.
     */
    public String col1Bubble1() {
        if (currentParticipant.getWpm(handler) < 110) {
            return "You spoke at a considerably slower speed than the expected average. Try to speak a little faster.";
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
     * Returns a string to put in a bubble that advises a user on how the effect of
     * improving their
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
     * Returns a string to put in a bubble that advises a user on their speech speed
     * compared to the
     * norm.
     * From:
     * https://cloudflare-ipfs.com/ipfs/QmXoypizjW3WknFiJnKLwHCnL72vedxjQkDDP1mXWo6uco/wiki/Words_per_minute.html
     * 
     * @return string the string to display in the bubble.
     */
    public String col3Bubble1() {
        return "Your word per minute speech speed (wpm) during this meeting was "
                + Math.round(currentParticipant.getWpm(handler))
                + ". A typical conversational speed is between 110 and 170 words per minute. Extremely fast speakers will reach 220+"
                + " words per minute.";
    }

    /**
     * Returns a string to put in a bubble, which tells a user about their sentence
     * sentiment.
     * 
     * @return the string to display in the bubble
     */
    public String col1Bubble2() {
        if (currentParticipant.getAverageSentiment() < -0.1) {
            return "Your overall speech sentiment during this meeting was negative. Try to use more positive words and to frame your "
                    + "discourse in a lighter manner.";
        } else if (currentParticipant.getAverageSentiment() >= -0.1
                && currentParticipant.getAverageSentiment() < 0.1) {
            return "Your overall speech sentiment during this meeting was roughly neutral. This is expected from a conversational"
                    + " type of meeting between two or more colleagues. If this was the case, good job!";
        } else if (currentParticipant.getAverageSentiment() >= 0.1 && currentParticipant.getAverageSentiment() < 0.25) {
            return "Your overall speech sentiment was positive during this meeting. This can be expected from less serious"
                    + " conversations, when light hearted topics are discussed, or simply if you were in a good mood."
                    + " If this was the case, good job!";
        } else {
            return "Your overall speech sentiment during this meeting was extremely positive. If this was intended, good job!";
        }
    }

    /**
     * Returns a string to put in a bubble, which tells a user how to adapt their
     * sentence sentiment.
     * 
     * @return the string to display in the bubble
     */
    public String col2Bubble2() {
        return "Picking words and formulating sentences that correctly match the thought and tone behind your discourse will allow your audience to"
                + " better understand the points you are trying to make and allow them to make the corresponding adjustments to their reactions"
                + " and responses. Intelligent choice of words can affect the sentiment of your discourse very easily, affecting the outcome of conversations.";
    }

    /**
     * Returns a string to put in a bubble, which tells a user how their sentence
     * sentiment was calculated
     * 
     * @return the string to display in the bubble
     */
    public String col3Bubble2() {
        return "Your spoken sentences were analysed using Natural Language Processing. Each spoken sentence was given a value: "
                + "either negative, neutral or positive. These values were assigned an integer equivalent, and an average value "
                + "was then calculated. your average sentiment value was "
                + String.format("%.2f", currentParticipant.getAverageSentiment())
                + ". This value can range from -1 to +1, but an average"
                + " value will usually be between -0.1 and +0.1.";
    }

    /**
     * Returns a string to put in a bubble, which informs a user about the amount of
     * filler words they used. based off of
     * https://schwa.byu.edu/files/2014/12/F2014-Robbins.pdf
     * 
     * @return the string to put in the bubble.
     */
    public String col1Bubble3() {

        Double fillerWordsPer100Words = 100 * currentParticipant.getFillerCount()
                / currentParticipant.getNumberOfWords();

        if (fillerWordsPer100Words < 1.28) {
            return "You used a very low amount of filler words during your meeting. Keep it up!";
        } else if (fillerWordsPer100Words >= 1.28
                && fillerWordsPer100Words < 2.17) {
            return "You used a low amount of filler words during your meeting, good job! You could still do a little better by cutting"
                    + " out a few.";
        } else if (fillerWordsPer100Words >= 2.17
                && fillerWordsPer100Words < 3.49) {
            return "You used an average amount of filler words during your meeting. Try to reduce your use of these"
                    + " words by more carefully thinking about your discourse before speaking, and taking the time to"
                    + " formulate the sentences in your head beforehand.";
        } else if (fillerWordsPer100Words >= 3.49
                && fillerWordsPer100Words < 5) {
            return "You used a high amount of filler words during your meeting. Try to heavily reduce your use of these"
                    + " words, by more carefully thinking about your discourse before speaking, and taking the time to"
                    + " formulate the sentences in your head beforehand.";
        } else {
            return "You used a very high amount of filler words during your meeting. Try to drastically reduce your use of these"
                    + " words, by more carefully thinking about your discourse before speaking, and taking the time to"
                    + " formulate the sentences in your head beforehand.";
        }
    }

    /**
     * A string that informs a user about how to alter their use of filler words.
     * 
     * @return the string to display in the bubble
     */
    public String col2Bubble3() {
        return "Use and particularly over-use of filler words can have a very negative impact on your discourse and its perception"
                + " by your audience. Studies have shown that filler words impact your credibility drastically. Effects also include "
                + "reduced ease of comprehension by your audience. Causes of excessive filler word use include divided attention from"
                + " a speaker, use of infrequent and unfamiliar words, and most frequently, nervousness.";
    }

    /**
     * A string that informs a user about the way their filler word rate was
     * calculated.
     * 
     * @return the string to display in the bubble
     */
    public String col3Bubble3() {
        Double fillerWordsPer100Words = 100 * currentParticipant.getFillerCount()
                / currentParticipant.getNumberOfWords();
        String valueToInsert = String.format("%.2f", fillerWordsPer100Words);
        return "Your speech was analysed and a \"filler words per hundred words\" value was then calculated. This metric has"
                + " been used in past studies to demonstrate the effect of filler words on speaker credibility. Your \"filler words per hundred words\""
                + " value was " + valueToInsert
                + ". Frederick Conrad et al. (2013) found that values above 1.28 had increasingly"
                + " negative effects on speaker credibility.";
    }

    /**
     * Returns a string to put in a bubble, which informs a user about the amount of
     * hesitation words they used. based off of
     * https://www.tandfonline.com/doi/abs/10.1080/01690968808402093
     * https://pubmed.ncbi.nlm.nih.gov/17173887/#:~:text=The%20study%20demonstrates%20that%20hesitation,the%20representation%20of%20the%20message.
     * 
     * @return the string to put in the bubble.
     */
    public String col1Bubble4() {
        Double hesitationsPer100Words = 100 * currentParticipant.getHesitationCount()
                / currentParticipant.getNumberOfWords();

        if (hesitationsPer100Words < 0.2) {
            return "You hesitated and paused very low amount of times during your meeting. Well done!";
        } else if (hesitationsPer100Words >= 0.2
                && hesitationsPer100Words < 0.4) {
            return "You hesitated and paused a low amount of times during your meeting, good job! You could still do a little better"
                    + " by more carefully thinking and planning out your discourse before speaking.";
        } else if (hesitationsPer100Words >= 0.4
                && hesitationsPer100Words < 0.7) {
            return "You hesitated and paused an average amount of times during your meeting. Try more carefully plan"
                    + " out your thoughts before speaking.";
        } else if (hesitationsPer100Words >= 0.7
                && hesitationsPer100Words < 1) {
            return "You hesitated and paused a high amount of times during your meeting. Try to make a significant effort to plan"
                    + " out your thoughts before speaking.";
        } else {
            return "You hesitated and paused a very high amount of times during your meeting. Try to make a very significant effort to plan"
                    + " out your thoughts before speaking.";
        }
    }

    /**
     * A string that informs a user about how they can change their use of
     * hesitations.
     * 
     * @return the string to display in the bubble
     */
    public String col2Bubble4() {
        return "Hesitations, also called non-lexical conversation sounds or more simply vocal pauses are often caused by"
                + " the same factors that cause filler words: nervousness and divided attention. Corley et al. (2007) "
                + "have shown excessive hesitation affects the way your discourse will be understood and processed, possibly"
                + " even altering their interpretation of your message.";
    }

    /**
     * Returns a string to put in a bubble, which informs a user about the amount of
     * hesitation words they used. based off of
     * https://www.tandfonline.com/doi/abs/10.1080/01690968808402093
     * 
     * @return the string to put in the bubble.
     */
    public String col3Bubble4() {
        Double hesitationsPer100Words = 100 * currentParticipant.getHesitationCount()
                / currentParticipant.getNumberOfWords();
        String valueToInsert = String.format("%.2f", hesitationsPer100Words);
        return "Your speech was analysed and a \"hesitations per hundred words\" value was then calculated. The transcript you "
                + "provided records a majority of your hesitations, logging words such as \"Uh\" or \"Hum\". Your \"hesitations per hundred words\""
                + " value was " + valueToInsert
                + ". Holmes (2007) found that average values for this metric range between 0.4 and 0.9.";
    }

    /**
     * Returns a string to put in a bubble, which informs a user about the share of
     * time they spoke. First the spoken sentence share is calculated for each user.
     * Then an expected share is calculated, as 100/number of participants. The
     * delta between these two values is then used to determine whether a user spoke
     * too much, too little, or juste the right amount.
     * 
     * @return the string to put in the bubble.
     */
    public String col1Bubble5() {
        int actualShare = (int) Math
                .round((currentParticipant.getNumberOfSentences() / handler.getSentences().size()) * 100);
        int expectedShare = Math.round(100 / handler.getParticipants().size());
        int delta = expectedShare - actualShare; // difference between expected sentences spoken and actual sentences
                                                 // spoken in percentage
        if (delta > expectedShare * 0.5) {
            return "speak lots more";
        } else if (delta <= expectedShare * 0.5
                && delta > expectedShare * 0.2) {
            return "speak more";
        } else if (delta <= expectedShare * 0.2
                && delta > -(expectedShare * 0.2)) {
            return "keep it up";
        } else if (delta <= -(expectedShare * 0.2)
                && delta > -(expectedShare * 0.5)) {
            return "speak less";
        } else {
            return "speak lots less";
        }
    }

    /**
     * Returns a string to put in a bubble, which informs a user about how to adapt
     * their speech volume in a conversation.
     * 
     * @return the string to put in the bubble.
     */
    public String col2Bubble5() {
        return "";
    }

    /**
     * Returns a string to put in a bubble, which informs a user about the share of
     * time they spoke.
     * 
     * @return the string to put in the bubble.
     */
    public String col3Bubble5() {
        int shareOfMeeting = (int) Math
                .round((currentParticipant.getNumberOfSentences() / handler.getSentences().size()) * 100);
        return "" + shareOfMeeting;
    }
}