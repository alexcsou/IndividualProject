package com.soloproject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.Desktop;

public class helpView {

    private Button HelpButton;

    public helpView() {
        HelpButton = new Button("Help");
        HelpButton.setOnAction(e -> showHelp());
    }

    /**
     * Method to show the help window to isntruct users on how to find and download
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

        Label instructionLabel1 = new Label(
                "𝗙𝗜𝗫𝗔𝗧𝗘𝗗 is primarily targeted at meetings recorded on Microsoft Teams."
                        + " Microsoft Teams meetings are automatically saved to Microsoft Stream, a video-sharing service."
                        + " 𝗙𝗜𝗫𝗔𝗧𝗘𝗗 provides an easy way to handle webTTV files, which are automatically created by Microsoft Stream."
                        + " A webTTV file produced from any other platform will also work but Microsoft Stream is the preferred option."
                        + "\n" + "\n"
                        + "1 - start by navigating to your Microsoft Stream dashboard at the following adress:"
                        + "\n");
        instructionLabel1.setWrapText(true);
        instructionLabel1.setMaxWidth(ScreenSizehandler.getWidth() / 2.5);
        instructionLabel1.setTextAlignment(TextAlignment.CENTER);

        Label instructionLabel2 = new Label(
                "2 - Once logged in, use the navigation bar at the top of the screen and click \"My content\"."
                        + " In the drop down menu that appears, select \"Videos\".");
        instructionLabel2.setWrapText(true);
        instructionLabel2.setTextAlignment(TextAlignment.CENTER);

        Label instructionLabel3 = new Label("The page that appears should then contain all your recordings." + "\n"
                + "\n"
                + "3 - Find the video you wish to get a transcript for, and click the \"Update video details\" icon to the right of the screen.");
        instructionLabel3.setWrapText(true);
        instructionLabel3.setTextAlignment(TextAlignment.CENTER);

        Label instructionLabel4 = new Label(
                "4 - Three panels should now be on screen. The right panel named \"Options\" should contain a \"Captions\" section."
                        + " Click the \"Download file\" hyperlink. Your .vtt file should then be automatically downloaded.");
        instructionLabel4.setWrapText(true);
        instructionLabel4.setTextAlignment(TextAlignment.CENTER);

        Image instructionImage1 = new Image(getClass().getResourceAsStream("images/instructions1.png"));
        ImageView instruction1View = new ImageView(instructionImage1);

        Image instructionImage2 = new Image(getClass().getResourceAsStream("images/instructions2.png"));
        ImageView instruction2View = new ImageView(instructionImage2);

        Image instructionImage3 = new Image(getClass().getResourceAsStream("images/instructions3.png"));
        ImageView instruction3View = new ImageView(instructionImage3);

        contentBox.getChildren().addAll(bigTitle, instructionLabel1, link, instructionLabel2, instruction1View,
                instructionLabel3, instruction2View, instructionLabel4, instruction3View);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.getStyleClass().add("scrollpane");
        scrollPane.setFitToWidth(true);
        helpPane.setCenter(scrollPane);

        Scene scene = new Scene(helpPane, ScreenSizehandler.getWidth() * 0.45, ScreenSizehandler.getHeight() * 0.8);
        scene.getStylesheets().add(getClass().getResource("styling/main.css").toExternalForm());
        Stage helpWindow = new Stage();
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
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Button getButton() {
        return HelpButton;
    }
}
