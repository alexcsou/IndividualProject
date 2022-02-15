package com.soloproject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.Desktop;

/**
 * The helpView class contains a view that a user is presented with upon
 * clicking "help" once Fixated is open. It contains a quick guide on how to
 * obtain a .vtt file.
 */
public class helpView {

    private Button HelpButton;
    private AlertHandler errorHandler = new AlertHandler(); // created to handle browser opening errors.

    // below are all the large strings of text displayed on the view.
    private final String instruction1 = new String(
            "𝗙𝗜𝗫𝗔𝗧𝗘𝗗 is primarily targeted at meetings recorded on Microsoft Teams."
                    + " Microsoft Teams meetings are automatically saved to Microsoft Stream, a video-sharing service."
                    + " 𝗙𝗜𝗫𝗔𝗧𝗘𝗗 provides an easy way to handle webTTV files, which are automatically created by Microsoft Stream."
                    + " A webTTV file produced from any other platform will also work but Microsoft Stream is the preferred option."
                    + "\n" + "\n"
                    + "1 - start by navigating to your Microsoft Stream dashboard at the following adress:"
                    + "\n");
    private final String instruction2 = new String(
            "2 - Once logged in, use the navigation bar at the top of the screen and click \"My content\"."
                    + " In the drop down menu that appears, select \"Videos\".");
    private final String instruction3 = new String("The page that appears should then contain all your recordings."
            + "\n"
            + "\n"
            + "3 - Find the video you wish to get a transcript for, and click the \"Update video details\" icon to the right of the screen.");
    private final String instruction4 = new String(
            "4 - Three panels should now be on screen. The right panel named \"Options\" should contain a \"Captions\" section."
                    + " Click the \"Download file\" hyperlink. Your .vtt file should then be automatically downloaded.");

    public helpView() {
        HelpButton = new Button("Help");
        HelpButton.setOnAction(e -> showHelp());
    }

    /**
     * Method to show the help window to instruct users on how to find and download
     * a .vtt of their transcript.
     */
    public void showHelp() {
        HelpButton.setDisable(true);
        BorderPane helpPane = new BorderPane();

        VBox contentBox = new VBox();
        contentBox.setAlignment(Pos.CENTER);

        Label bigTitle = new Label("How do I get a meeting transcript?");
        bigTitle.getStyleClass().add("bigTitle");

        Hyperlink link = new Hyperlink();
        link.setText("https://web.microsoftstream.com/");
        link.setOnAction(e -> openBrowser(link.getText()));

        Label instructionLabel1 = new Label(instruction1);
        instructionLabel1.setWrapText(true);
        instructionLabel1.setMaxWidth(ScreenSizehandler.getWidth() / 2.5); // large label, restrict its size so that
                                                                           // doesn't span the whole screen if app is
                                                                           // fullscreened.
        instructionLabel1.setTextAlignment(TextAlignment.CENTER);

        Label instructionLabel2 = new Label(instruction2);
        instructionLabel2.setWrapText(true);
        instructionLabel2.setTextAlignment(TextAlignment.CENTER);

        Label instructionLabel3 = new Label(instruction3);
        instructionLabel3.setWrapText(true);
        instructionLabel3.setTextAlignment(TextAlignment.CENTER);

        Label instructionLabel4 = new Label(instruction4);
        instructionLabel4.setWrapText(true);
        instructionLabel4.setTextAlignment(TextAlignment.CENTER);

        Image instructionImage1 = new Image(getClass().getResourceAsStream("images/instructions1.png")); // helper
                                                                                                         // screenshot
        ImageView instruction1View = new ImageView(instructionImage1);

        Image instructionImage2 = new Image(getClass().getResourceAsStream("images/instructions2.png")); // helper
                                                                                                         // screenshot
        ImageView instruction2View = new ImageView(instructionImage2);

        Image instructionImage3 = new Image(getClass().getResourceAsStream("images/instructions3.png"));// helper
                                                                                                        // screenshot
        ImageView instruction3View = new ImageView(instructionImage3);

        contentBox.getChildren().addAll(bigTitle, instructionLabel1, link, instructionLabel2, instruction1View,
                instructionLabel3, instruction2View, instructionLabel4, instruction3View);

        contentBox.getChildren().forEach(c -> c.getStyleClass().add("helpViewItem")); // style all items on the helpview

        helpPane.setCenter(contentBox);
        // size the window that opens up to not take too much space and show that it
        // overlaps the previous one by making it's width thinner.
        Scene scene = new Scene(helpPane, ScreenSizehandler.getWidth() * 0.45, ScreenSizehandler.getHeight() * 0.8);
        scene.getStylesheets().add(getClass().getResource("styling/main.css").toExternalForm());

        Stage helpWindow = new Stage();
        helpWindow.setMinHeight(ScreenSizehandler.getHeight() * 0.8); // disables height resizing.
        helpWindow.setTitle(".vtt Import help");

        helpWindow.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.png")));
        helpWindow.setScene(scene);
        helpWindow.showAndWait();// disable help button until user closes help window
        HelpButton.setDisable(false);
    }

    /**
     * A method that opens a link in a browser. Taken from
     * https://stackoverflow.com/a/47645799
     * 
     * @param link the adress to visit.
     */
    public void openBrowser(String link) {
        try {
            Desktop.getDesktop().browse(new URL(link).toURI());
        } catch (IOException e) {
            errorHandler.alertFailure("Couldn't open your browser.");
        } catch (URISyntaxException e) {
            errorHandler.alertFailure("Couldn't open your browser.");
        }
    }

    /**
     * 
     * @return the help button, entry point to this view
     */
    public Button getButton() {
        return HelpButton;
    }
}
