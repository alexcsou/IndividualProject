package com.soloproject;

import javafx.event.EventHandler;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DashboardView {

    private TranscriptHandler handler;
    private FlowPane mainPane;

    public DashboardView(TranscriptHandler handler) {
        this.handler = handler;
        mainPane = new FlowPane();
    }

    public FlowPane getView() {
        handler.createParticipantsSentences(); // create participants and their data, ready to be displayed.
        makeView(); // called when creating the tabs.
        return mainPane;
    }

    public void makeView() {
        mainPane.getChildren().add(getParticipationPieChart());
    }

    public StackPane getParticipationPieChart() {

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Share of Spoken Sentences");
        pieChart.getStyleClass().addAll("chart");
        for (Participant p : handler.getParticipants()) {

            int percentage = (int) Math.round((p.getNumberOfSentences() / handler.getSentences().size()) * 100);

            PieChart.Data data = new PieChart.Data(percentage + "% " + p.getName(), p.getNumberOfSentences());
            pieChart.getData().add(data);

        }

        return new StackPane(pieChart);
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