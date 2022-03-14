package com.soloproject;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * This is a class which contains various methods to handle errors in the App.
 * These methods create visible alerts that notify users of issues.
 */
public class AlertHandler {

        public AlertHandler() {
        }

        /**
         * A method to display a failure alert after any operation.
         * 
         * @param message the message to display in an alert after an error occurs.
         */
        public void alertFailure(String message) {
                Alert failureAlert = new Alert(AlertType.ERROR);
                failureAlert.getDialogPane().getStylesheets()
                                .add(getClass().getResource("styling/main.css").toExternalForm());// specify styling
                                                                                                  // file location
                failureAlert.getDialogPane().getStyleClass().add("alert");// style alert only
                failureAlert.setTitle("Something went wrong...");
                failureAlert.setContentText(message);
                failureAlert.setHeaderText("Something went wrong...");
                failureAlert.showAndWait()
                                .filter(response -> response == ButtonType.OK)
                                .ifPresent(response -> failureAlert.close());// wait until alert is closed or accepted.
        }

        /**
         * A method to display a success alert after selecting a file.
         */
        public void alertSuccess(String message) {
                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.getDialogPane().getStylesheets()
                                .add(getClass().getResource("styling/main.css").toExternalForm());// specify styling
                                                                                                  // file location
                successAlert.getDialogPane().getStyleClass().add("alert");// style alert only
                successAlert.setTitle("Operation Successful");
                successAlert.setContentText(message);
                successAlert.setHeaderText("Operation Successful");
                successAlert.showAndWait()
                                .filter(response -> response == ButtonType.OK)
                                .ifPresent(response -> successAlert.close());// wait until alert is closed or accepted,
                                                                             // and open the
                                                                             // dashboard
        }

        /**
         * A method to display a warning to users about potentially not matching files.
         */
        public void warnAboutWrongFiles() {
                Alert successAlert = new Alert(AlertType.WARNING);
                successAlert.getDialogPane().getStylesheets()
                                .add(getClass().getResource("styling/main.css").toExternalForm());// specify styling
                                                                                                  // file location
                successAlert.getDialogPane().getStyleClass().add("alert");// style alert only
                successAlert.setTitle("Possible file selection error");
                successAlert.setContentText(
                                "It's likely that your two files don't match or that you've swapped the Teams file with the Stream file. Please make sure you've selected the correct files.");
                successAlert.setHeaderText("Possible erroneous file choice");
                successAlert.showAndWait()
                                .filter(response -> response == ButtonType.OK)
                                .ifPresent(response -> successAlert.close());// wait until alert is closed or accepted
        }

        /**
         * A method to display a loading alert while a file is being processed.
         */
        public void alertLoading() {
                Alert loadingAlert = new Alert(AlertType.INFORMATION);
                loadingAlert.getDialogPane().getStylesheets()
                                .add(getClass().getResource("styling/main.css").toExternalForm());// specify styling
                                                                                                  // file location
                loadingAlert.getDialogPane().getStyleClass().add("alert");// style alert only
                loadingAlert.setTitle("Your File is About to be Processed");
                loadingAlert.setContentText(
                                "Your file is about to be processed. This can take upwards of several minutes on slower devices. "
                                                + "Please wait for an alert before proceeding. Do not close 𝗙𝗜𝗫𝗔𝗧𝗘𝗗.");
                loadingAlert.setHeaderText("Preparing to load file...");
                loadingAlert.showAndWait()
                                .filter(response -> response == ButtonType.OK)
                                .ifPresent(response -> loadingAlert.close());// wait until alert is closed or accepted,
                                                                             // and open the
                                                                             // dashboard

        }
}
