package com.soloproject;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;

/**
 * A view that a user is presented with when on the dashboard tab. It contains
 * all the charts and KPIs about a meeting on a scrollable tilepane. It is
 * loaded in MainView.
 */
public class DashboardView {

    private TranscriptHandler handler;
    private TilePane mainPane;

    public DashboardView(TranscriptHandler handler) {
        this.handler = handler;

        mainPane = new TilePane();
        mainPane.setPadding(new Insets(10, 10, 10, 10));
        mainPane.setPrefTileWidth(ScreenSizeHandler.getWidth() * 0.46); // sized so two tiles are always shown on a row
        mainPane.setVgap(4);
        mainPane.setHgap(4);

    }

    public ScrollPane getView() {
        handler.createParticipantsSentences(); // create participants and their data, ready to be displayed.
        makeView(); // called when creating the tabs.

        ScrollPane returnPane = new ScrollPane(); // return the mainPane in a scrollpane to make it scrollable
        returnPane.getStyleClass().add("scrollPane");
        returnPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        returnPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        returnPane.setContent(mainPane);
        returnPane.setMaxHeight(ScreenSizeHandler.getHeight() * 0.92);
        returnPane.setMaxWidth(ScreenSizeHandler.getWidth() * 1);

        return returnPane;
    }

    public void makeView() {
        // add tiles to the tilepane, create all charts via method calls
        mainPane.getChildren().add(getKPIs());
        mainPane.getChildren().add(getParticipationPieChart());
        mainPane.getChildren().add(getParticipationPieChart2());
        mainPane.getChildren().add(getParticipationPieChart3());
        mainPane.getChildren().add(getSentenceTypePieChart());
        mainPane.getChildren().add(getSentenceTypeBarChart());
        mainPane.getChildren().add(getSentimentalAnalysisChart());
        mainPane.getChildren().add(getSentimentBarChart());
        mainPane.getChildren().add(getWpmBarChart());
        mainPane.getChildren().add(getHesitationAndFillerBarChart());

        // dimensions to be device screen size agnostic
        mainPane.setMaxHeight(ScreenSizeHandler.getHeight() * 0.92);
        mainPane.setMaxWidth(ScreenSizeHandler.getWidth() * 1);
    }

    /**
     * 
     * @return TilePane the mainpane
     */
    public TilePane getMainPane() {
        return mainPane;
    }

    /**
     * Returns the first chart of the app, containing 6 metrics about the meeting
     * loaded in a gridpane and then put into a tile of a tilepane
     * 
     * @return GridPane the gripd cotnaining the metrics
     */
    public GridPane getKPIs() {

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        Label one = new Label("Meeting Duration:\n\n" + handler.getMeetingDurationString());
        gridPane.add(one, 0, 0);

        GridPane.setHalignment(one, HPos.CENTER); // adding these two here on top of the lambda (see below) because
        GridPane.setValignment(one, VPos.CENTER); // the Lambda doesnt affect this cell for some reason

        one.setTextAlignment(TextAlignment.CENTER);
        one.setWrapText(true);

        Label two = new Label("Number of Participants:\n\n" + handler.getParticipants().size());
        gridPane.add(two, 2, 0);
        two.setTextAlignment(TextAlignment.CENTER);
        two.setWrapText(true);

        Label three = new Label(
                "Transcript Accuracy:\n\n" + Math.round(handler.getTranscriptRecognizabilityDouble() * 100) + "%");
        if (handler.getTranscriptRecognizabilityDouble() == 0.0) {
            three.setText("Transcript Accuracy:\n\nNot Found.");
        }
        three.setTextAlignment(TextAlignment.CENTER);
        three.setWrapText(true);

        gridPane.add(three, 4, 0);

        Label four = new Label("Number of Spoken Sentences:\n\n" + handler.getFullSentenceCount());
        gridPane.add(four, 0, 2);
        four.setTextAlignment(TextAlignment.CENTER);
        four.setWrapText(true);

        Label five = new Label("Number of Spoken Words:\n\n" + handler.getNumberOfWords());
        gridPane.add(five, 2, 2);
        five.setTextAlignment(TextAlignment.CENTER);
        five.setWrapText(true);

        Label six = new Label("Meeting Language:\n\n" + handler.getLanguage());
        gridPane.add(six, 4, 2);
        six.setTextAlignment(TextAlignment.CENTER);
        six.setWrapText(true);

        // labels added, now add separators for better readability

        gridPane.add(getSep(Orientation.HORIZONTAL), 0, 1);
        gridPane.add(getSep(Orientation.HORIZONTAL), 2, 1);
        gridPane.add(getSep(Orientation.HORIZONTAL), 4, 1);

        gridPane.add(getSep(Orientation.VERTICAL), 1, 0);
        gridPane.add(getSep(Orientation.VERTICAL), 1, 2);
        gridPane.add(getSep(Orientation.VERTICAL), 3, 0);
        gridPane.add(getSep(Orientation.VERTICAL), 3, 2);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.getRowConstraints().forEach(s -> s.setValignment(VPos.CENTER));
        gridPane.getColumnConstraints().forEach(s -> s.setHalignment(HPos.CENTER));
        gridPane.setId("kpis");

        return gridPane;
    }

    /**
     * Returns the second chart, a pie chart showing the share of spoken sentences
     * by each participant.
     * 
     * @return PieChart, the javafx Node to display in a TilePane tile
     */
    public PieChart getParticipationPieChart() {

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Share of Spoken Sentences");
        pieChart.getStyleClass().addAll("chart");
        pieChart.setLabelsVisible(true);

        // create data point for each participant
        for (Participant p : handler.getParticipants()) {

            int percentage = (int) Math.round((p.getNumberOfSentences() / handler.getSentences().size()) * 100);

            PieChart.Data data = new PieChart.Data(percentage + "% - " + p.getName(), p.getNumberOfSentences());
            pieChart.getData().add(data);

            // add tooltip for each participant giving the details of the metric
            Tooltip t = new Tooltip(
                    "Share of spoken sentences: " + percentage + "%" + "\n" + "Number of spoken sentences: "
                            + p.getNumberOfSentences());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(data.getNode(), t);
        }

        return pieChart;
    }

    /**
     * The third chart, returning a piechart showing the share of spoken words for
     * each participant
     * 
     * @return the PieChart javafx Node to display in the Tilepane tile.
     */
    public PieChart getParticipationPieChart2() {

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Share of Spoken Words");
        pieChart.getStyleClass().addAll("chart");
        pieChart.setLabelsVisible(true);

        // add data for each participant
        for (Participant p : handler.getParticipants()) {

            int percentage = (int) Math.round((p.getNumberOfWords() / handler.getNumberOfWords()) * 100);

            PieChart.Data data = new PieChart.Data(percentage + "% - " + p.getName(), p.getNumberOfWords());
            pieChart.getData().add(data);

            // add tooltips
            Tooltip t = new Tooltip("Share of spoken words: " + percentage + "%" + "\n" + "Number of spoken words: "
                    + p.getNumberOfWords());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(data.getNode(), t);
        }

        return pieChart;
    }

    /**
     * The third pie chart, showing the share of spoken time for each
     * participant, and including an extra data point for silence, calculated as the
     * total meeting duration minus the sum of spoken time by all participants.
     * 
     * @return the PieChart to display
     */
    public PieChart getParticipationPieChart3() {

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Share of Spoken Time");
        pieChart.getStyleClass().addAll("chart");
        pieChart.setLabelsVisible(true);

        Double nonSilenceTime = 0.0; // used to track silence in share of time distribution

        // add data for each participant
        for (Participant p : handler.getParticipants()) {

            // keep track of spoken time for each participant
            nonSilenceTime += p.getSpokenTime();
            // display the spoken time as percentage of meeting duration
            int percentage = (int) Math.round((p.getSpokenTime()) / handler.getMeetingDurationSeconds() * 100);

            PieChart.Data data = new PieChart.Data(percentage + "% - " + p.getName(), p.getSpokenTime());
            pieChart.getData().add(data);

            // add tooltips
            Tooltip t = new Tooltip("Share of spoken time: " + percentage + "%" + "\n" + "Time spoken in seconds: "
                    + p.getSpokenTime());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(data.getNode(), t);
        }

        // add data for silence, substracting all spoken time from total meeting time
        int percentage = (int) Math.round(
                (handler.getMeetingDurationSeconds() - nonSilenceTime) / handler.getMeetingDurationSeconds() * 100);

        PieChart.Data data = new PieChart.Data(percentage + "% " + "Silence",
                handler.getMeetingDurationSeconds() - nonSilenceTime);
        pieChart.getData().add(data);

        // add tooltip for silence
        Tooltip t = new Tooltip(
                "Share of meeting spent in silence: " + percentage + "%" + "\n" + "Time in seconds spent in silence: "
                        + (handler.getMeetingDurationSeconds() - nonSilenceTime));
        t.getStyleableParent().getStyleClass().clear();
        Tooltip.install(data.getNode(), t);

        return pieChart;
    }

    /**
     * fourth and last PieChart, returning the share of each sentence type used in
     * the meeting, which can be either declarative, interrogative, exclamatory or
     * other.
     * 
     * @return the piechart to display in the tilepane.
     */
    public PieChart getSentenceTypePieChart() {

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Sentence Type Counts");
        pieChart.getStyleClass().addAll("chart");
        pieChart.setLabelsVisible(true);

        // use these values to later add to the chart
        int declaratives = 0;
        int exclamatories = 0;
        int interrogatives = 0;
        int others = 0;

        // iterate through sentences and increment relevant tracking variable
        for (TranscriptSentence s : handler.getSentences()) {

            if (s.getSentenceType().equals("Declarative")) {
                declaratives += 1;
            } else if (s.getSentenceType().equals("Exclamatory")) {
                exclamatories += 1;
            } else if (s.getSentenceType().equals("Interrogative")) {
                interrogatives += 1;
            } else if (s.getSentenceType().equals("Other")) {
                others += 1;
            }
        }
        // For each sentence type, add data point only if value is not 0
        if (declaratives != 0) {
            PieChart.Data data = new PieChart.Data(declaratives + " declarative sentences",
                    declaratives);
            pieChart.getData().add(data);
        }

        if (exclamatories != 0) {
            PieChart.Data data1 = new PieChart.Data(exclamatories + " exclamatory sentences",
                    exclamatories);
            pieChart.getData().add(data1);
        }

        if (interrogatives != 0) {
            PieChart.Data data2 = new PieChart.Data(interrogatives + " interrogative sentences", interrogatives);
            pieChart.getData().add(data2);
        }

        if (others != 0) {
            PieChart.Data data3 = new PieChart.Data(others + " other sentence types", others);
            pieChart.getData().add(data3);
        }

        // add tooltips
        for (PieChart.Data d : pieChart.getData()) {
            Tooltip t = new Tooltip(d.getName());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(d.getNode(), t);
        }

        return pieChart;
    }

    /**
     * A method that returns a bar chart which lists the count of each sentence type
     * for each user
     * 
     * @return a bar chart to display in a TilePane
     */
    public BarChart<String, Number> getSentenceTypeBarChart() {

        CategoryAxis xAxis = new CategoryAxis(); // for participant names
        NumberAxis yAxis = new NumberAxis(); // for sentence types
        xAxis.setLabel("Sentence Type per Participant");
        yAxis.setLabel("Number of Sentences");

        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis,
                yAxis);

        barChart.setTitle("Sentence type Distribution per Participant");
        barChart.setLegendSide(Side.BOTTOM);

        // one serie per sentence type
        XYChart.Series<String, Number> Declaratives = new XYChart.Series<>();
        Declaratives.setName("Declarative");

        XYChart.Series<String, Number> Interrogatives = new XYChart.Series<>();
        Interrogatives.setName("Interrogative");

        XYChart.Series<String, Number> Exclamatories = new XYChart.Series<>();
        Exclamatories.setName("Exclamatory");

        barChart.getData().addAll(Declaratives, Interrogatives, Exclamatories);

        // add data point for each serie for each participant.
        for (Participant p : handler.getParticipants()) {
            Declaratives.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getDeclaratives().size()));

            Interrogatives.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getInterrogatives().size()));

            Exclamatories.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getExclamatories().size()));

        }

        // add all tooltips
        for (XYChart.Data<String, Number> d : Declaratives.getData()) {
            Tooltip t = new Tooltip("Number of Declarative Sentences: " + d.getYValue());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(d.getNode(), t);
        }

        for (XYChart.Data<String, Number> d : Interrogatives.getData()) {
            Tooltip t = new Tooltip("Number of Interrogatives Sentences: " + d.getYValue());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(d.getNode(), t);
        }

        for (XYChart.Data<String, Number> d : Exclamatories.getData()) {
            Tooltip t = new Tooltip("Number of Exclamatory Sentences: " + d.getYValue());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(d.getNode(), t);
        }

        barChart.setId("verticalBarChart");
        return barChart;

    }

    /**
     * A method that returns a line chart with a data point for each sentence spoken
     * by each user, with a negative, neutral and positive sentences being numbered
     * -1, 0 and 1 respectively. To visualise the average value for each user, the
     * average is calculated, and then a data point equal to that value is palced
     * both at the start and end of the chart.
     * 
     * @return the line chart to display in a tilepane
     */
    public LineChart<Number, Number> getSentimentalAnalysisChart() {

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (seconds)");
        yAxis.setLabel("Sentiment Rating (-1 to 1)");

        LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Sentence Sentiment Over Time");
        lineChart.setLegendSide(Side.BOTTOM);
        lineChart.setCreateSymbols(false); // hide circle data point icon as heavy loads of points clutter chart

        // new series for each participant
        for (Participant p : handler.getParticipants()) {
            XYChart.Series<Number, Number> SentimentValues = new XYChart.Series<>();
            SentimentValues.setName(p.getName());

            // new data point for each sentence for each participant
            for (TranscriptSentence s : p.getSentences()) {
                SentimentValues.getData()
                        .add(new XYChart.Data<>(s.getStartTime(), s.getSentimentRating()));
            }

            // calculate average sentence sentiment
            p.setAverageSentiment();

            // adding the average line by setting a value at the first and last x values
            XYChart.Series<Number, Number> avgSentimentValues = new XYChart.Series<>();
            avgSentimentValues.setName(p.getName() + " -  Average sentiment value ("
                    + String.format("%.2f", p.getAverageSentiment()) + ")"); // round to 2 decimal places
            avgSentimentValues.getData()
                    .add(new XYChart.Data<>(0, p.getAverageSentiment()));
            avgSentimentValues.getData()
                    .add(new XYChart.Data<>(
                            // add second data point at the x position equal to end time of last sentence in
                            // list
                            handler.getSentences().get(handler.getSentences().size() - 1).getEndTime(),
                            p.getAverageSentiment()));

            // add both series
            lineChart.getData().add(SentimentValues);
            lineChart.getData().add(avgSentimentValues);
        }

        return lineChart;
    }

    /**
     * A method that returns a bar chart with for each participant, a bar indicating
     * the number of negative, neutral and positive sentences.
     * 
     * @return the bar chart to display in a tile of a tilepane.
     */
    public BarChart<String, Number> getSentimentBarChart() {

        CategoryAxis xAxis = new CategoryAxis(); // for participant names
        NumberAxis yAxis = new NumberAxis(); // for sentence counts
        xAxis.setLabel("Sentence Sentiment per Participant");
        yAxis.setLabel("Number of Sentences");

        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis,
                yAxis);

        barChart.setTitle("Sentence Sentiment Distribution per Participant");
        barChart.setLegendSide(Side.BOTTOM);

        // one serie per sentence type
        XYChart.Series<String, Number> Positives = new XYChart.Series<>();
        Positives.setName("Positive sentences");

        XYChart.Series<String, Number> Neutrals = new XYChart.Series<>();
        Neutrals.setName("Neutral sentences");

        XYChart.Series<String, Number> Negatives = new XYChart.Series<>();
        Negatives.setName("Negative sentences");

        barChart.getData().addAll(Positives, Neutrals, Negatives);

        // add data point for each serie for each participant.
        for (Participant p : handler.getParticipants()) {
            Positives.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getPositives().size()));

            Neutrals.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getNeutrals().size()));

            Negatives.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getNegatives().size()));

        }

        // add all tooltips
        for (XYChart.Data<String, Number> d : Positives.getData()) {
            Tooltip t = new Tooltip("Number of Positive Sentences: " + d.getYValue());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(d.getNode(), t);
        }

        for (XYChart.Data<String, Number> d : Neutrals.getData()) {
            Tooltip t = new Tooltip("Number of Neutral Sentences: " + d.getYValue());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(d.getNode(), t);
        }

        for (XYChart.Data<String, Number> d : Negatives.getData()) {
            Tooltip t = new Tooltip("Number of Negative Sentences: " + d.getYValue());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(d.getNode(), t);
        }

        barChart.setId("verticalBarChart");
        return barChart;

    }

    /**
     * A method that returns a stackedbar chart which shows a bar equal to the word
     * per minute speed of each participant.
     * 
     * @return the bar chart to display in the tilepane tile
     */
    public StackedBarChart<Number, String> getWpmBarChart() {

        CategoryAxis yAxis = new CategoryAxis();
        NumberAxis xAxis = new NumberAxis();
        yAxis.setLabel("Participant");
        xAxis.setLabel("Words per Minute");
        xAxis.setTickLabelRotation(90);

        // using stacked bar chart as normal bar chart off-centers the bar as it expects
        // at least two serie types.
        StackedBarChart<Number, String> barChart = new StackedBarChart<Number, String>(xAxis,
                yAxis);

        barChart.setTitle("Speech Rate per User");
        barChart.setLegendSide(Side.BOTTOM);

        for (Participant p : handler.getParticipants()) {

            // One series per participant
            XYChart.Series<Number, String> series = new XYChart.Series<>();
            series.setName("WPM - " + p.getName());
            series.getData().add(new XYChart.Data<Number, String>(
                    p.getWpm(handler), p.getName()));
            barChart.getData().add(series);

            // add tooltips
            for (XYChart.Data<Number, String> d : series.getData()) {
                Tooltip t = new Tooltip("WPM - " + d.getYValue() + ": " + d.getXValue());
                t.getStyleableParent().getStyleClass().clear();
                Tooltip.install(d.getNode(), t);
            }

        }

        barChart.setId("horizontalBarChart");
        return barChart;

    }

    /**
     * a method that returns a bar chart with one series per user for each
     * hesitation made, and one series for each filler word used. Hesitations and
     * filler words are listed in the transcriptsentence class.
     * 
     * @return the bar chart with both series for each user.
     */
    public BarChart<String, Number> getHesitationAndFillerBarChart() {

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Filler Words and Hesitation Count per Participant");
        yAxis.setLabel("Number of Occurences");

        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis,
                yAxis);

        barChart.setTitle("Count of Filler Words and Hesitations per Participant");
        barChart.setLegendSide(Side.BOTTOM);

        XYChart.Series<String, Number> FillerWords = new XYChart.Series<>();
        FillerWords.setName("Filler Words");

        XYChart.Series<String, Number> Hesitations = new XYChart.Series<>();
        Hesitations.setName("Hesitations");

        barChart.getData().addAll(FillerWords, Hesitations);

        // add counts for each user.
        for (Participant p : handler.getParticipants()) {
            FillerWords.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getFillerCount()));

            Hesitations.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getHesitationCount()));

        }
        // add tooltips
        for (XYChart.Data<String, Number> d : FillerWords.getData()) {
            Tooltip t = new Tooltip("Number of Filler Words: " + d.getYValue());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(d.getNode(), t);
        }

        for (XYChart.Data<String, Number> d : Hesitations.getData()) {
            Tooltip t = new Tooltip("Number of Hesitations: " + d.getYValue());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(d.getNode(), t);
        }

        barChart.setId("fillersAndHesitations");
        return barChart;

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
        smallSep.setId("bigSep");
        return smallSep;
    }
}
