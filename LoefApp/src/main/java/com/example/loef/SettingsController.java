package com.example.loef;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class SettingsController {

    @FXML
    private ComboBox<String> resolutionComboBox;

    @FXML
    private HBox mainHBox;

    private final ResolutionController resolutionManager = ResolutionController.getInstance();

    @FXML
    public void initialize() {
        // Laad opgeslagen resolutie
        resolutionComboBox.setValue(resolutionManager.getCurrentResolution());
        applyResolution(resolutionManager.getCurrentResolution());

        // Luister naar wijzigingen in de ComboBox
        resolutionComboBox.setOnAction(event -> {
            String selectedResolution = resolutionComboBox.getValue();
            resolutionManager.setResolution(selectedResolution);
            applyResolution(selectedResolution);
        });
    }

    private void applyResolution(String resolution) {
        String[] dimensions = resolution.split("x");
        if (dimensions.length == 2) {
            try {
                double width = Double.parseDouble(dimensions[0]);
                double height = Double.parseDouble(dimensions[1]);
                mainHBox.setPrefWidth(width);
                mainHBox.setPrefHeight(height);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
