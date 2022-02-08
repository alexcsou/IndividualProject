package com.soloproject;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class ErrorHandler {

    public ErrorHandler() {

    }

    /**
     * A method to display a failure alert after any operation.
     */
    public void alertFailure(String message) {
        Alert failureAlert = new Alert(AlertType.ERROR);
        failureAlert.getDialogPane().getStylesheets()
                .add(getClass().getResource("styling/main.css").toExternalForm());// specify styling file location
        failureAlert.getDialogPane().getStyleClass().add("alert");// style alert only
        failureAlert.setTitle("Something went wrong...");
        failureAlert.setContentText(message);
        failureAlert.setHeaderText("Something went wrong...");
        failureAlert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> failureAlert.close());// wait until alert is closed or accepted.
    }
}
