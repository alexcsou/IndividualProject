package com.soloproject;

import javafx.stage.Screen;

/**
 * Static class that fetches screen dimensions. Code for dimensions taken from
 * https://stackoverflow.com/a/40320750
 */
public class ScreenSizehandler {

    private static int width = (int) Screen.getPrimary().getBounds().getWidth();
    private static int height = (int) Screen.getPrimary().getBounds().getHeight();

    public ScreenSizehandler() {
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
