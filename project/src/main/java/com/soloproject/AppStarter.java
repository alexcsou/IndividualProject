package com.soloproject;

/**
 * This is a starter class that works as a workaround to a quirk of Maven used
 * with JavaFX, which requires the main method to be outside the class that
 * contains the launch() method. Run this class to run the app, not the App
 * class.
 */
public class AppStarter {
    public static void main(final String[] args) {
        App.main(args);
    }

}
