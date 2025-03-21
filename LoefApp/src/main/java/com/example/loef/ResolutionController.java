package com.example.loef;

import java.util.prefs.Preferences;

public class ResolutionController {
    private static final ResolutionController INSTANCE = new ResolutionController();
    private final Preferences prefs;
    private String currentResolution;

    private ResolutionController() {
        prefs = Preferences.userNodeForPackage(ResolutionController.class);
        currentResolution = prefs.get("resolution", "1920x1080");
    }

    public static ResolutionController getInstance() {
        return INSTANCE;
    }

    public String getCurrentResolution() {
        return currentResolution;
    }

    public void setResolution(String resolution) {
        currentResolution = resolution;
        prefs.put("resolution", resolution);
    }
}

