package com.soloproject;

import java.text.DecimalFormat;

import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;

import javafx.scene.layout.TilePane;

public class DashboardView {

    private TranscriptHandler handler;
    private TilePane mainPane;

    public DashboardView(TranscriptHandler handler) {
        this.handler = handler;

        mainPane = new TilePane();
        mainPane.setPadding(new Insets(10, 10, 10, 10));
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
        mainPane.getChildren().add(getParticipationPieChart());
        mainPane.getChildren().add(getParticipationPieChart2());
        mainPane.getChildren().add(getParticipationPieChart3());
        mainPane.getChildren().add(getSentenceTypePieChart());
        mainPane.getChildren().add(getSentimentalAnalysisChart());
        mainPane.getChildren().add(getSentimentBarChart());
        mainPane.setMaxHeight(ScreenSizehandler.getHeight() * 0.92);
        mainPane.setMaxWidth(ScreenSizehandler.getWidth() * 1);
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

        setSize(pieChart);
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

        setSize(pieChart);
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

        setSize(pieChart);
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

        setSize(pieChart);
        return pieChart;
    }

    public LineChart<Number, Number> getSentimentalAnalysisChart() {

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (seconds)");
        yAxis.setLabel("Sentiment Rating (0-1)");

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

        setSize(lineChart);
        return lineChart;
    }

    public BarChart<String, Number> getSentimentBarChart() {

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Sentence Sentiment per User");
        yAxis.setLabel("Number of Sentences");

        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis,
                yAxis);

        barChart.setTitle("Sentence Sentiment Distribution per User");
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

        setSize(barChart);
        return barChart;

    }

    public void setSize(Chart chart) {
        chart.setMinHeight(ScreenSizehandler.getHeight() * 0.50);
        chart.setMaxHeight(ScreenSizehandler.getHeight() * 0.50);
        chart.setMinWidth(ScreenSizehandler.getHeight() * 0.72);
        chart.setMaxWidth(ScreenSizehandler.getHeight() * 0.72);
    }
}
