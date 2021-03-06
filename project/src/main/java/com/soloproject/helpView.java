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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.Desktop;

/**
 * The HelpView class contains a view that a user is presented with upon
 * clicking "help" once Fixated is open. It contains a quick guide on how to
 * obtain a .vtt file from both platforms.
 */
public class HelpView {

    private BorderPane helpPane = new BorderPane();
    private Button next;
    private Button previous;
    private Button HelpButton;
    private AlertHandler errorHandler = new AlertHandler(); // created to handle browser opening errors.

    // below are all the large strings of text displayed on the views.

    private final String instruction1 = new String(
            "𝗙𝗜𝗫𝗔𝗧𝗘𝗗 is targeted at meetings recorded on Microsoft Teams."
                    + " To make full use of 𝗙𝗜𝗫𝗔𝗧𝗘𝗗, you should provide two transcripts of the same meeting."
                    + " The first file can be downloaded from Microsoft Stream, a video-sharing service."
                    + " A second file is also required, which can be downloaded directly from Microsoft Teams."
                    + " For a detailed guide, please also refer to the next page."
                    + "\n\n"
                    + "1 - start by navigating to your Microsoft Stream dashboard at the following adress:"
                    + "\n");

    private final String instruction2 = new String(
            "2 - Once logged in, use the navigation bar at the top of the screen and click \"My content\"."
                    + " In the drop down menu that appears, select \"Videos\".");

    private final String instruction3 = new String("The page that appears should then contain all your recordings."
            + "\n"
            + "3 - Find the video you wish to get a transcript for, and click the \"Update video details\" icon to the right of the screen.");

    private final String instruction4 = new String(
            "4 - Three panels should now be on screen. The right panel named \"Options\" should contain a \"Captions\" section."
                    + " Click the \"Download file\" hyperlink. \n Your .vtt file should then be automatically downloaded."
                    +
                    "\nPlease note your file's name should normally end with \"_AutoGeneratedCaption.vtt\".");

    private final String instruction5 = new String(
            "The second transcript can be downloaded on Microsoft Teams. Both the desktop app"
                    + " or the web version will work. "
                    + " Microsoft Teams transcripts are generated whenever a meeting is recorded."
                    + " These transcripts appear once a meeting is over in the chat log of that meeting."
                    + " The transcript can then be downloaded directly from the chat in either .vtt or .docx format."
                    + "\n \n"
                    + "1 - start by opening your Microsoft Teams dashboard. The sidebar should contain a \"Chat\" section."
                    + "\n");

    private final String instruction6 = new String(
            "2 - Open the chat section. Search for the chat associated to your meeting in the list. \n"
                    + "3 - Once that chat is selected, search the chat log for a meeting transcript item, like this one:");

    private final String instruction7 = new String(
            "4 - Click the \"three dots\" icon in the top right of item, and select \"Download as .vtt\"."
                    + " Your file should be automatically downloaded."
                    + "\nPlease note your file's name should normally end with the date the meeting took place on, in the 'YYY-MM-DD' format.");

    public HelpView() {
        HelpButton = new Button("Help");
        HelpButton.setOnAction(e -> showHelp());

        // set up next and prev buttons
        next = new Button("Next");
        next.setOnAction(e -> next());

        previous = new Button("Previous");
        previous.setOnAction(e -> previous());

    }

    /**
     * Method to show the help window to instruct users on how to find and download
     * a .vtt of their transcript.
     */
    public void showHelp() {
        HelpButton.setDisable(true);

        previous.setDisable(true);
        AnchorPane navButtons = new AnchorPane(previous, next);
        AnchorPane.setLeftAnchor(previous, 10.0);
        AnchorPane.setRightAnchor(next, 10.0);
        helpPane.setBottom(navButtons);

        // initialise window on microsoft stream guide
        helpPane.setCenter(getStreamGuide());

        Scene scene = new Scene(helpPane);
        scene.getStylesheets().add(getClass().getResource("styling/main.css").toExternalForm());

        Stage helpWindow = new Stage();
        helpWindow.setResizable(false);
        helpWindow.setTitle(".vtt Import help");

        helpWindow.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.png")));
        helpWindow.setScene(scene);
        helpWindow.showAndWait();// disable help button until user closes help window
        HelpButton.setDisable(false);
    }

    /**
     * A method that returns a VBox containing the tutorial a user should follow to
     * download a stream file
     * 
     * @return a VBox containing all isntructions as images and labels
     */
    public VBox getStreamGuide() {

        VBox contentBox = new VBox();
        contentBox.setAlignment(Pos.CENTER);

        Label bigTitle = new Label("Getting a Microsoft Stream Transcript");
        bigTitle.getStyleClass().add("bigTitle");

        Hyperlink link = new Hyperlink();
        link.setText("https://web.microsoftstream.com/");
        link.setOnAction(e -> openBrowser(link.getText()));

        // create all labels and align them to center of screen.
        Label instructionLabel1 = new Label(instruction1);
        instructionLabel1.setWrapText(true);
        instructionLabel1.setMaxWidth(ScreenSizeHandler.getWidth() / 2.5);
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

        return contentBox;
    }

    /**
     * A method that returns a VBox containing all the items necessary to teach usrs
     * how to acess the transcript of their meeting from microsoft teams
     * 
     * @return the VBox containing all necessary items, labels and pictures, to
     *         instruct users.
     */
    public VBox getTeamsGuide() {
        VBox contentBox = new VBox();
        contentBox.setAlignment(Pos.CENTER);

        Label bigTitle = new Label("Getting a Microsoft Teams transcript");
        bigTitle.getStyleClass().add("bigTitle");

        Label instructionLabel1 = new Label(instruction5);
        instructionLabel1.setWrapText(true);
        instructionLabel1.setMaxWidth(ScreenSizeHandler.getWidth() / 2.5); // large label, restrict its size so that
                                                                           // doesn't span the whole screen if app is
                                                                           // fullscreened.
        instructionLabel1.setTextAlignment(TextAlignment.CENTER);

        Label instructionLabel2 = new Label(instruction6);
        instructionLabel2.setWrapText(true);
        instructionLabel2.setTextAlignment(TextAlignment.CENTER);

        Label instructionLabel3 = new Label(instruction7);
        instructionLabel3.setWrapText(true);
        instructionLabel3.setTextAlignment(TextAlignment.CENTER);

        Image instructionImage1 = new Image(getClass().getResourceAsStream("images/instructions4.jpg")); // helper
        // screenshot

        ImageView instruction1View = new ImageView(instructionImage1);

        Image instructionImage2 = new Image(getClass().getResourceAsStream("images/instructions5.jpg")); // helper
        // screenshot
        ImageView instruction2View = new ImageView(instructionImage2);

        Image instructionImage3 = new Image(getClass().getResourceAsStream("images/instructions6.jpg"));// helper
        // screenshot
        ImageView instruction3View = new ImageView(instructionImage3);

        contentBox.getChildren().addAll(bigTitle, instructionLabel1, instruction1View,
                instructionLabel2, instruction2View, instructionLabel3, instruction3View);

        contentBox.getChildren().forEach(c -> c.getStyleClass().add("helpViewItem")); // style all items on the helpview

        return contentBox;
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
     * Method called when pressing the next button, loads the teams guide
     */
    public void next() {
        helpPane.setCenter(getTeamsGuide());
        next.setDisable(true);
        previous.setDisable(false);

    }

    /**
     * Method called when pressing the previous button, loads the stream guide.
     */
    public void previous() {
        helpPane.setCenter(getStreamGuide());
        previous.setDisable(true);
        next.setDisable(false);
    }

    /**
     * 
     * @return the help button, entry point to this view
     */
    public Button getButton() {
        return HelpButton;
    }
}
