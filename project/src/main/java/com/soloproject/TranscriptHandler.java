package com.soloproject;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

import javafx.scene.control.Button;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * TranscriptHandler is a class responsible for all operations to do with the
 * .vtt transcript provided by the user. It generates TranscriptSentence items
 * stored in a "sentences" arraylist. It also extracts the meeting duration and
 * the transcript recognizability, as well as its language. It also provides
 * the button that opens a file chooser.
 */
public class TranscriptHandler {

    private Button streamButton;
    private Button teamsButton;
    private Button continueButton;
    private Stage stage;
    private File streamTranscript = null;
    private File teamsTranscript = null;
    private ArrayList<TranscriptSentence> sentences;
    private ArrayList<Participant> participants;
    private String meetingDurationString; // stored as string as displayed in .vtt file
    private double meetingDurationSeconds; // converts string duration to sum of seconds
    private String transcriptRecognizability; // value out of 1
    private String language; // usually en-us or en-uk.
    private AlertHandler alertHandler = new AlertHandler();

    /**
     * Used for testing
     */
    public TranscriptHandler() {
        sentences = new ArrayList<>();
        participants = new ArrayList<>();

        meetingDurationString = "";
        meetingDurationSeconds = 0.0;
        transcriptRecognizability = "";
        language = "";
    }

    public TranscriptHandler(Stage stage) {
        streamButton = new Button("Choose a Microsoft Stream file");
        streamButton.setOnAction(e -> chooseStreamFile());
        teamsButton = new Button("Choose a Microsoft Teams file");
        teamsButton.setOnAction(e -> chooseTeamsFile());
        continueButton = new Button("Proceed to dashboard");
        continueButton.setDisable(true);
        continueButton.setOnAction(e -> proceed());

        this.stage = stage;

        sentences = new ArrayList<>();
        participants = new ArrayList<>();

        meetingDurationString = "";
        meetingDurationSeconds = 0.0;
        transcriptRecognizability = "";
        language = "";
    }

    /**
     * A method to open a file chooser in an OS native look. Restricted to .vtt
     * files. Sets the transcript file and calls handlefile().
     */
    public void chooseStreamFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a .vtt file downloaded from Microsoft Stream");

        chooser.getExtensionFilters().addAll(
                new ExtensionFilter("WebVTT Files", "*.vtt"));
        File newTranscript = chooser.showOpenDialog(stage);
        if (newTranscript != null) {
            setStreamTranscript(newTranscript);
            generateMeetingData();
            alertHandler.alertSuccess("File successfully loaded.");
            streamButton.setText("Stream file Selected");
            streamButton.setDisable(true);

            if (teamsTranscript != null) {
                continueButton.setDisable(false); // allow user to proceed if both files are selected
                if (potentialError()) {
                    alertHandler.warnAboutWrongFiles();
                }
            }

        } else {
            alertHandler.alertFailure("No file was processed.");
        }
    }

    /**
     * A method to open a file chooser in an OS native look. Restricted to .vtt
     * files. Sets the transcript file and calls handlefile().
     */
    public void chooseTeamsFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a .vtt file downloaded from Microsoft Teams");

        chooser.getExtensionFilters().addAll(
                new ExtensionFilter("WebVTT Files", "*.vtt"));
        File newTranscript = chooser.showOpenDialog(stage);
        if (newTranscript != null) {
            setTeamsTranscript(newTranscript);
            teamsButton.setText("Loading. Please Wait");
            teamsButton.setDisable(true);
            streamButton.setDisable(true);

            alertHandler.alertLoading(); // alert about long loading
            generateSentences();
            alertHandler.alertSuccess("File successfully loaded.");
            teamsButton.setText("Teams file Selected");
            streamButton.setDisable(false);

            if (streamTranscript != null) {
                continueButton.setDisable(false); // allow user to proceed if both files are selected
                if (potentialError()) {
                    alertHandler.warnAboutWrongFiles();
                }
            }
        } else {
            alertHandler.alertFailure("No file was processed.");
        }
    }

    /**
     * A method that guess whether a user may have inputted two erroneous files
     * based on file names, and flags whether there is a potential issue.
     * 
     * @return true if there is a potential error, false otherwise
     */
    public boolean potentialError() {
        Pattern pattern = Pattern.compile("\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])");
        Matcher matcher1 = pattern.matcher(teamsTranscript.getName());
        Matcher matcher2 = pattern.matcher(streamTranscript.getName());

        String meetingName = streamTranscript.getName().replace("_AutoGeneratedCaption.vtt", "");

        // check the selected stream file has the expected form, same for Teams file +
        // check that both files contain the same meeting name as substrings
        if (teamsTranscript.getName().contains("AutoGeneratedCaption")
                || !matcher1.find() || !streamTranscript.getName().contains("AutoGeneratedCaption")
                || matcher2.find() || !teamsTranscript.getName().contains(meetingName)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * A method which loads the main view into the scene and fullscreens the app
     */
    public void loadMainView() {
        stage.getScene().setRoot(new MainView(stage, this).getView());// change root pane of scene to that of MainView
        stage.setFullScreen(true);
    }

    /**
     * Method which generates TranscriptSentences objetcs, which are composed of
     * lines extracted from the transcript: the confidence, the timestamp and the
     * text itself.
     */
    public void generateSentences() {
        try {
            Scanner reader = new Scanner(teamsTranscript);
            ArrayList<String> groupof2 = new ArrayList<>(); // create an array for storing lines during extraction
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (isValid(line)) { // check line isn't filler text or irrelevant
                    groupof2.add(line);
                    if (groupof2.size() == 2) { // as soon as the array reaches size 3, we create a sentence

                        sentences.add(generateSentence(extractAuthor(groupof2.get(1)), groupof2.get(0),
                                extractSentence(groupof2.get(1))));
                        groupof2.clear(); // empty array and repeat until EOF
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            alertHandler.alertFailure("Your file couldn't be found");
        }
    }

    /**
     * Method which extracts the author of a sentence
     * 
     * @param input the sentence to extract from
     * @return the name of the other formatted as "FirstName LastName"
     */
    public String extractAuthor(String input) {
        Pattern pattern = Pattern.compile("<v (.*?)>");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            if (!matcher.group(1).contains(",")) { // guest users are formatted differently, we handle it here
                return matcher.group(1);
            } else {
                String[] split = matcher.group(1).split(", "); // split first and last name
                return (split[1] + " " + split[0]); // return in order of first and last name
            }
        } else {
            return ("Speaker not found");
        }
    }

    /**
     * Method which extracts the sentence from a line of .vtt file from Teams
     * 
     * @param input the line of the file
     * @return the sentence without the author and the <v> </v> tags.
     */
    public String extractSentence(String input) {
        Pattern pattern = Pattern.compile(">(.*?)</v>");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return ("Sentence not found");
        }
    }

    /**
     * Returns all transcript sentences concatenated as a single String
     * 
     * @return all sentences cocnatenated as a single string
     */
    public String getSentencesAsSingleString() {
        String returnString = "";
        for (TranscriptSentence s : sentences) {
            returnString += s.getSentence();
        }
        return returnString;
    }

    /**
     * A method that counts the number of spoken words in a transcript.
     * 
     * @return the number of words as an int.
     */
    public int getNumberOfWords() {
        int count = 0;
        for (TranscriptSentence s : getSentences()) {
            count += s.getSentence().split(" ").length;
        }
        return count;
    }

    /**
     * A method to check if a line read from the .vtt file is either text, a time
     * stamp for the text line, or the confidence for that line. Excludes lines
     * giving general meeting data as well as ID lines which seem to appear on
     * transcripts for meetings lasting more than 1 min.
     * 
     * @param line the transcript line to check
     * @return true if the line is to be incorporated into a transcript sentence,
     *         false otherwise.
     */
    public boolean isValid(String line) {
        return !(line.contains("WEBVTT") || line.contains("NOTE duration:") || line.contains("NOTE recognizability:")
                || line.contains("NOTE language:") || isIDLine(line) || line.isBlank());
    }

    /**
     * A method that checks whether a transcript line is an ID line, which are
     * present on transcripts for meetings lasting over a few seconds (1min+).
     * An example of an ID line is : "dbcf586a-c9b6-461e-ba75-b76a72a8d398"
     * 
     * @param input the line to check
     * @return true if the line is an id line, false otherwise.
     */
    public boolean isIDLine(String input) {
        Pattern pattern = Pattern.compile("(.*?)-(.*?)-(.*?)-(.*?)-(.*?)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method that extracts and sets the meeting duration, transcript
     * recognizability and meeting language. Uses regex and the Pattern and Matcher
     * classes to identify them.
     */
    public void generateMeetingData() {
        boolean durationSet = false;
        boolean recogSet = false;
        boolean languageSet = false;
        try {
            Scanner reader = new Scanner(streamTranscript);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.contains("NOTE duration:") && !durationSet) { // extra !durationSet condition in rare edge
                                                                       // cases where transcript contains the specified
                                                                       // string in line.contains()
                    Pattern pattern = Pattern.compile("\\d+:\\d+:\\d+");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        setMeetingDurationString(matcher.group(0)); // group(0) is the first match (and only match in
                                                                    // our case)
                        setMeetingDurationSeconds(secondParser(matcher.group(0))); // we have the duration string nicely
                                                                                   // formatted above, now we convert
                                                                                   // that duration string into a double
                                                                                   // amount of seconds
                        durationSet = true;
                    }
                } else if (line.contains("NOTE recognizability:") && !recogSet) {

                    Pattern pattern = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        setTranscriptRecognizability(matcher.group(0));
                        recogSet = true;
                    }
                } else if (line.contains("NOTE language:") && !languageSet) {

                    line = line.replace("NOTE language:", "");
                    setLanguage(line);
                    languageSet = true;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            alertHandler.alertFailure("Your file couldn't be found");
        } catch (Exception e) {
            alertHandler.alertFailure("something went wrong when processing your file...");
        }
    }

    /**
     * method inspired by: https://stackoverflow.com/q/34331637
     * A method to convert a string of the form "00:00:04.220" into a duration of
     * seconds.
     * 
     * @param input
     * @return Double the duration in seconds
     */
    public double secondParser(String input) {
        try {
            String[] duration = input.split(":"); // split on : divider
            double HH = Double.parseDouble(duration[0]);
            double mm = Double.parseDouble(duration[1]);
            double ss = Double.parseDouble(duration[2]);
            return (HH * 3600 + mm * 60 + ss); // convert the hours and minutes to seconds, sum and return as double

        } catch (Exception e) {
            return 0.0;
        }

    }

    /**
     * A method that creates a TranscriptSentence object from the provided strings.
     * Converts the timestamp line into an array of doubles, with index 0 as the
     * start and index 1 as the end. Also parses the double from the "confidence"
     * string
     * 
     * @param duration a timestamp for the text line of the form "00:00:02.220 -->
     *                 00:00:04.220"
     * @param text     the spoken line as a string
     * @return a TranscriptSentence object with all its fields initialised.
     */
    public TranscriptSentence generateSentence(String author, String duration, String text) {
        ArrayList<Double> durationValues = getDurationDataToDouble(duration);
        return new TranscriptSentence(text, duration, durationValues.get(0), durationValues.get(1),
                author);
    }

    /**
     * A method to convert a time stamp (00:00:02.220 --> 00:00:04.220) into an
     * array of doubles of size two, with the values being the start and end times
     * of the timestamp in seconds since the start of the meeting.
     * 
     * @param durationLine a String of the form 00:00:02.220 --> 00:00:04.220
     * @return an arraylist of doubles with (if all goes well) two values, start and
     *         end of timestamp
     */
    public ArrayList<Double> getDurationDataToDouble(String durationLine) {
        ArrayList<Double> list = new ArrayList<>();
        String[] durations = durationLine.split("-->");
        try {
            double startTime = secondParser(durations[0]);
            double endTime = secondParser(durations[1]);
            list.add(startTime);
            list.add(endTime);
        } catch (NumberFormatException e) {
            list.add(0.0);
            list.add(0.0);
            return list;
        } catch (ArrayIndexOutOfBoundsException e) {
            list.add(0.0);
            list.add(0.0);
            return list;
        }

        return list;
    }

    /**
     * A method that counts the number of sentences, that is lines of vtt text that
     * end with a sentence punctuation.
     * 
     * @return the number of spoken sentences in the meeting.
     */
    public int getFullSentenceCount() {
        int count = 0;
        for (TranscriptSentence s : sentences) {
            if (s.getSentence().endsWith(".") || s.getSentence().endsWith("?") || s.getSentence().endsWith("!")
                    || s.getSentence().endsWith("...")) {
                count++;
            }
        }
        // failsafe against div by 0
        if (count == 0) {
            return 1;
        } else {
            return count;
        }
    }

    /**
     * A method that loads the mainview or alerts a user somehting went wrong after
     * selecting files.
     */
    public void proceed() {
        if (streamTranscript != null && teamsTranscript != null) {
            loadMainView();
        } else {
            alertHandler.alertFailure("You haven't selected the necessary files to proceed.");
        }
    }

    /**
     * Getter for the stream file select button
     * 
     * @return Button
     */
    public Button getStreamFileSelectButton() {
        return streamButton;
    }

    /**
     * Getter for the teams file select button
     * 
     * @return Button
     */
    public Button getTeamsFileSelectButton() {
        return teamsButton;
    }

    /**
     * Getter for the transcript recognizability value.
     * 
     * @return Double the recognisability.
     */
    public Double getTranscriptRecognizabilityDouble() {
        try {
            return Double.parseDouble(this.transcriptRecognizability);
        } catch (NumberFormatException e) {
            // no alert as it appears mutliple times.
            return 0.0;
        }

    }

    /**
     * Mehtod to return the Stream file
     * 
     * @return the .vtt Microsoft Stream transcript file
     */
    public File getStreamTranscript() {
        return streamTranscript;
    }

    /**
     * Method to return the teams file
     * 
     * @return the .vtt Teams transcript file
     */
    public File getTeamsTranscript() {
        return teamsTranscript;
    }

    /**
     * create and returns a list of transcript bubbles created from the list of
     * transcript sentences generated prior. This method also styles each bubble,
     * with odd bubbles being styled to be left aligned and vice versa for even
     * bubbles. This gives a "chat" look to the transcript.
     * 
     * @return the list of transcriptbubble nodes as an array.
     */
    public ArrayList<TranscriptBubble> getBubbles() {
        ArrayList<TranscriptBubble> list = new ArrayList<>();

        try {
            for (int i = 0; i < sentences.size() - 1; i += 2) {
                list.add(new TranscriptBubble(sentences.get(i), false)); // true or false determine styling
                list.add(new TranscriptBubble(sentences.get(i + 1), true));
            }
            if (!(sentences.size() % 2 == 0)) { // if uneven number of sentences, add last sentence here to avoid oob
                                                // exception
                list.add(new TranscriptBubble(sentences.get(sentences.size() - 1), false));
            }
        } catch (Exception e) {
            System.out.println(e);
            list.clear();
            list.add(new TranscriptBubble(
                    new TranscriptSentence("your sentences Couldn't be loaded.", "", 0, 0, ""), true));
        }
        return list;
    }

    /**
     * Stream file setter
     * 
     * @param file the file to set as stream file
     */
    public void setStreamTranscript(File file) {
        this.streamTranscript = file;
    }

    /**
     * Teams file setter
     * 
     * @param file the file to set as Teams file
     */
    public void setTeamsTranscript(File file) {
        this.teamsTranscript = file;
    }

    /**
     * Getter for stream button
     * 
     * @return Buttont
     */
    public Button getStreamButton() {
        return this.streamButton;
    }

    /**
     * Getter for teams button
     * 
     * @return Buttont
     */
    public Button getTeamsButton() {
        return this.teamsButton;
    }

    /**
     * Getter for continue button
     * 
     * @return Buttont
     */
    public Button getContinueButton() {
        return this.continueButton;
    }

    /**
     * Setter for Stream button
     * 
     * @param button
     */
    public void setStreamButton(Button button) {
        this.streamButton = button;
    }

    /**
     * getter for the stage
     * 
     * @return Stage the stage
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Setter for the stage
     * 
     * @param stage the Stage to set as stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Getter for the list of transcript sentences
     * 
     * @return an array list of sentences
     */
    public ArrayList<TranscriptSentence> getSentences() {
        return this.sentences;
    }

    /**
     * Setter for the array list of sentences
     * 
     * @param sentences the arraylist of sentence to set
     */
    public void setSentences(ArrayList<TranscriptSentence> sentences) {
        this.sentences = sentences;
    }

    /**
     * Getter for the meeting duration string
     * 
     * @return the meeting duration string field
     */
    public String getMeetingDurationString() {
        return this.meetingDurationString;
    }

    /**
     * Setter for the meeting duration string
     * 
     * @param meetingDurationString the string to set
     */
    public void setMeetingDurationString(String meetingDurationString) {
        this.meetingDurationString = meetingDurationString;
    }

    /**
     * Getter for transcript recog field
     * 
     * @return the string value of the transcript recog
     */
    public String getTranscriptRecognizability() {
        return this.transcriptRecognizability;
    }

    /**
     * Setter for the transcript recog
     * 
     * @param transcriptRecognizability string to set
     */
    public void setTranscriptRecognizability(String transcriptRecognizability) {
        this.transcriptRecognizability = transcriptRecognizability;
    }

    /**
     * getter for the meeting language.
     * 
     * @return ther string meeting language
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for the meeting language
     * 
     * @param language the string to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Getter for the list of participants
     * 
     * @return arraylist of participants
     */
    public ArrayList<Participant> getParticipants() {
        return this.participants;
    }

    /**
     * Getter for the meeting duration string.
     * 
     * @return 1.0 if the duration is 0, the duration otherwise. 1 is returned to
     *         avoid division by 0 and handle meeting duration calculations
     */
    public Double getMeetingDurationSeconds() {
        if (this.meetingDurationSeconds == 0) {
            return 1.0;
        } else {
            return this.meetingDurationSeconds;
        }
    }

    /**
     * Setter for the meeting duration in seconds
     * 
     * @param meetingDurationSeconds the double to set
     */
    public void setMeetingDurationSeconds(double meetingDurationSeconds) {
        this.meetingDurationSeconds = meetingDurationSeconds;
    }

    /**
     * Return a string of all speakers in a meeting, and importantly create
     * Participant objects,
     * 1 per speaker
     * 
     * @return a string formatted to display speakers properly, of the form 'X, Y,
     *         Z'
     */
    public String getSpeakers() {
        ArrayList<String> speakers = new ArrayList<>();
        String speakersString = new String();
        for (TranscriptSentence s : sentences) {
            if (!speakers.contains(s.getAuthor())) {
                speakers.add(s.getAuthor());
                participants.add(new Participant(s.getAuthor()));
            }
        }

        for (String s : speakers) {
            speakersString += s + ", ";
        }

        try {
            return speakersString.substring(0, speakersString.length() - 2); // remove final ',' separator
        } catch (Exception e) {
            return "No Speakers Found.";
        }

    }

    /**
     * A method that iterates through the transcript sentences and adds each
     * sentence to it's respective participant's arrayList of sentences.
     * Called within the Dashboard view.
     */
    public void createParticipantsSentences() {
        for (TranscriptSentence s : sentences) {
            for (Participant p : participants) {
                if (s.getAuthor().equals(p.getName())) {
                    p.addSentence(s);
                }
            }
        }

    }

}
