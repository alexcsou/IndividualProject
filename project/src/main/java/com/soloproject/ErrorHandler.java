package com.soloproject;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * This is a class which contains various methods to handle errors in the App.
 * These methods create visible alerts that notify users of issues.
 */
public class ErrorHandler {

    public ErrorHandler() {
    }

    /**
     * A method to display a failure alert after any operation.
     * 
     * @param message the message to display in an alert after an error occurs.
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
