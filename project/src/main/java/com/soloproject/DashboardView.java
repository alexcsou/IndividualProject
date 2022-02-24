package com.soloproject;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;

public class DashboardView {

    private TranscriptHandler handler;
    private TilePane mainPane;

    public DashboardView(TranscriptHandler handler) {
        this.handler = handler;

        mainPane = new TilePane();
        mainPane.setPadding(new Insets(10, 10, 10, 10));
        mainPane.setVgap(4);
        mainPane.setHgap(4);
        mainPane.setPrefTileHeight(270);
        mainPane.setPrefTileWidth(360);

    }

    public TilePane getView() {
        handler.createParticipantsSentences(); // create participants and their data, ready to be displayed.
        makeView(); // called when creating the tabs.
        return mainPane;
    }

    public void makeView() {
        mainPane.getChildren().add(getParticipationPieChart());
        mainPane.getChildren().add(getParticipationPieChart2());
        mainPane.getChildren().add(getParticipationPieChart3());
        mainPane.getChildren().add(getSentenceTypePieChart());
    }

    public PieChart getParticipationPieChart() {

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Share of Spoken Sentences");
        pieChart.getStyleClass().addAll("chart");
        pieChart.setLabelsVisible(false);

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
        pieChart.setLabelsVisible(false);

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
        pieChart.setLabelsVisible(false);

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
        pieChart.setTitle("Sentence Type Distribution");
        pieChart.getStyleClass().addAll("chart");
        pieChart.setLabelsVisible(false);

        Double declaratives = 0.0;
        Double exclamatories = 0.0;
        Double interrogatives = 0.0;
        Double others = 0.0;

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

}

// NumberAxis xAxis = new NumberAxis();
// NumberAxis yAxis = new NumberAxis();
// xAxis.setLabel("Time (seconds)");
// yAxis.setLabel("Participation");
// // creating the chart
// final LineChart<Number, Number> lineChart = new LineChart<Number,
// Number>(xAxis, yAxis);

// lineChart.setTitle("Test chart");
// // defining a series
// for (Participant p : handler.getParticipants()) {
// XYChart.Series<Number, Number> series = new XYChart.Series<>();
// series.setName(p.getName());
// series.getData().add(new XYChart.Data<>(0, 1));
// lineChart.getData().add(series);
// }