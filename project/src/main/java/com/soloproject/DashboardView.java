package com.soloproject;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
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

public class DashboardView {

    private TranscriptHandler handler;
    private TilePane mainPane;

    public DashboardView(TranscriptHandler handler) {
        this.handler = handler;

        mainPane = new TilePane();
        mainPane.setPadding(new Insets(10, 10, 10, 10));
        mainPane.setPrefTileWidth(ScreenSizehandler.getWidth() * 0.46);
        mainPane.setVgap(4);
        mainPane.setHgap(4);

    }

    public ScrollPane getView() {
        handler.createParticipantsSentences(); // create participants and their data, ready to be displayed.
        makeView(); // called when creating the tabs.

        ScrollPane returnPane = new ScrollPane();
        returnPane.getStyleClass().add("TranscriptScrollPane");
        returnPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        returnPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        returnPane.setContent(mainPane);
        returnPane.setMaxHeight(ScreenSizehandler.getHeight() * 0.92);
        returnPane.setMaxWidth(ScreenSizehandler.getWidth() * 1);

        return returnPane;
    }

    public void makeView() {
        // add tiles to the tilepane
        mainPane.getChildren().add(getKPIs());
        mainPane.getChildren().add(getParticipationPieChart());
        mainPane.getChildren().add(getParticipationPieChart2());
        mainPane.getChildren().add(getParticipationPieChart3());
        mainPane.getChildren().add(getSentenceTypePieChart());
        mainPane.getChildren().add(getSentimentalAnalysisChart());
        mainPane.getChildren().add(getSentimentBarChart());
        mainPane.getChildren().add(getWpmBarChart());
        mainPane.getChildren().add(getHesitationAndFillerBarChart());

        mainPane.setMaxHeight(ScreenSizehandler.getHeight() * 0.92);
        mainPane.setMaxWidth(ScreenSizehandler.getWidth() * 1);
    }

    public TilePane getMainPane() {
        return mainPane;
    }

    public GridPane getKPIs() {

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        Label one = new Label("Meeting Duration:\n\n" + handler.getMeetingDurationString());
        gridPane.add(one, 0, 0);

        GridPane.setHalignment(one, HPos.CENTER); // adding these two here on top of the lambda because
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

    public PieChart getParticipationPieChart() {

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Share of Spoken Sentences");
        pieChart.getStyleClass().addAll("chart");
        pieChart.setLabelsVisible(true);

        for (Participant p : handler.getParticipants()) {

            int percentage = (int) Math.round((p.getNumberOfSentences() / handler.getSentences().size()) * 100);

            PieChart.Data data = new PieChart.Data(percentage + "% - " + p.getName(), p.getNumberOfSentences());
            pieChart.getData().add(data);

            Tooltip t = new Tooltip(
                    "Share of spoken sentences: " + percentage + "%" + "\n" + "Number of spoken sentences: "
                            + p.getNumberOfSentences());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(data.getNode(), t);
        }

        return pieChart;
    }

    public PieChart getParticipationPieChart2() {

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Share of Spoken Words");
        pieChart.getStyleClass().addAll("chart");
        pieChart.setLabelsVisible(true);

        for (Participant p : handler.getParticipants()) {

            int percentage = (int) Math.round((p.getNumberOfWords() / handler.getNumberOfWords()) * 100);

            PieChart.Data data = new PieChart.Data(percentage + "% - " + p.getName(), p.getNumberOfWords());
            pieChart.getData().add(data);

            Tooltip t = new Tooltip("Share of spoken words: " + percentage + "%" + "\n" + "Number of spoken words: "
                    + p.getNumberOfWords());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(data.getNode(), t);
        }

        return pieChart;
    }

    public PieChart getParticipationPieChart3() {

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Share of Spoken Time");
        pieChart.getStyleClass().addAll("chart");
        pieChart.setLabelsVisible(true);

        Double nonSilenceTime = 0.0; // used to track silence in share of time distribution
        for (Participant p : handler.getParticipants()) {

            nonSilenceTime += p.getSpokenTime();
            int percentage = (int) Math.round((p.getSpokenTime()) / handler.getMeetingDurationSeconds() * 100);

            PieChart.Data data = new PieChart.Data(percentage + "% - " + p.getName(), p.getSpokenTime());
            pieChart.getData().add(data);

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

        Tooltip t = new Tooltip(
                "Share of meeting spent in silence: " + percentage + "%" + "\n" + "Time in seconds spent in silence: "
                        + (handler.getMeetingDurationSeconds() - nonSilenceTime));
        t.getStyleableParent().getStyleClass().clear();
        Tooltip.install(data.getNode(), t);

        return pieChart;
    }

    public PieChart getSentenceTypePieChart() {

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Sentence Type Counts");
        pieChart.getStyleClass().addAll("chart");
        pieChart.setLabelsVisible(true);

        int declaratives = 0;
        int exclamatories = 0;
        int interrogatives = 0;
        int others = 0;

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

        for (PieChart.Data d : pieChart.getData()) {
            Tooltip t = new Tooltip(d.getName());
            t.getStyleableParent().getStyleClass().clear();
            Tooltip.install(d.getNode(), t);
        }

        return pieChart;
    }

    public LineChart<Number, Number> getSentimentalAnalysisChart() {

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (seconds)");
        yAxis.setLabel("Sentiment Rating (-1 to 1)");

        LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Sentence Sentiment Over Time");
        lineChart.setLegendSide(Side.BOTTOM);
        lineChart.setCreateSymbols(false);

        for (Participant p : handler.getParticipants()) {
            XYChart.Series<Number, Number> SentimentValues = new XYChart.Series<>();
            SentimentValues.setName(p.getName());

            for (TranscriptSentence s : p.getSentences()) {
                SentimentValues.getData()
                        .add(new XYChart.Data<>(s.getStartTime(), s.getSentimentRating()));
            }

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

            lineChart.getData().add(SentimentValues);
            lineChart.getData().add(avgSentimentValues);
        }

        return lineChart;
    }

    public BarChart<String, Number> getSentimentBarChart() {

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Sentence Sentiment per Participant");
        yAxis.setLabel("Number of Sentences");

        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis,
                yAxis);

        barChart.setTitle("Sentence Sentiment Distribution per Participant");
        barChart.setLegendSide(Side.BOTTOM);

        XYChart.Series<String, Number> Positives = new XYChart.Series<>();
        Positives.setName("Positive sentences");

        XYChart.Series<String, Number> Neutrals = new XYChart.Series<>();
        Neutrals.setName("Neutral sentences");

        XYChart.Series<String, Number> Negatives = new XYChart.Series<>();
        Negatives.setName("Negative sentences");

        barChart.getData().addAll(Positives, Neutrals, Negatives);

        for (Participant p : handler.getParticipants()) {
            Positives.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getPositives().size()));

            Neutrals.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getNeutrals().size()));

            Negatives.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getNegatives().size()));

        }

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

    public StackedBarChart<Number, String> getWpmBarChart() {

        CategoryAxis yAxis = new CategoryAxis();
        NumberAxis xAxis = new NumberAxis();
        yAxis.setLabel("Participant");
        xAxis.setLabel("Words per Minute");
        xAxis.setTickLabelRotation(90);
        StackedBarChart<Number, String> barChart = new StackedBarChart<Number, String>(xAxis,
                yAxis);

        barChart.setTitle("Speech speed per user");
        barChart.setLegendSide(Side.BOTTOM);

        for (Participant p : handler.getParticipants()) {
            XYChart.Series<Number, String> series = new XYChart.Series<>();
            series.setName("WPM - " + p.getName());
            series.getData().add(new XYChart.Data<Number, String>(
                    p.getWpm(handler), p.getName()));
            barChart.getData().add(series);

            for (XYChart.Data<Number, String> d : series.getData()) {
                Tooltip t = new Tooltip("WPM - " + d.getYValue() + ": " + d.getXValue());
                t.getStyleableParent().getStyleClass().clear();
                Tooltip.install(d.getNode(), t);
            }

        }

        barChart.setId("horizontalBarChart");
        return barChart;

    }

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

        for (Participant p : handler.getParticipants()) {
            FillerWords.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getFillerCount()));

            Hesitations.getData().add(new XYChart.Data<String, Number>(p.getName(),
                    p.getHesitationCount()));

        }

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
}
