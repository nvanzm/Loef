package com.example.loef.controllers;

import com.example.loef.models.DataUren;
import com.example.loef.models.Werknemer;
import com.example.loef.models.WerknemerData;
import com.example.loef.util.JsonService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WerkgeverUrenController {

    @FXML
    private Label naamLabel;

    @FXML
    private Label urenLabel;

    @FXML
    private Label loonLabel;

    @FXML
    private ListView<String> werknemersLijst;

    @FXML
    private HBox mainHBox;

    @FXML
    private ComboBox<String> maandSelectie;
    public String geselecteerdeMaand = "April";

    public static final String MAP_NAAM = "maandenData/";

    private final WerknemerData dataService = WerknemerData.getInstance();
    private final ResolutionController resolutionManager = ResolutionController.getInstance();

    Werknemer werknemer;

    @FXML
    public void initialize() {
        for (Werknemer werknemer : dataService.getWerknemers()) {
            werknemersLijst.getItems().add(werknemer.getNaam());
        }

        applyResolution(resolutionManager.getCurrentResolution());
        configureMaandSelectieListener();
    }

    @FXML
    public void selecteerWerknemer() {
        String geselecteerdeNaam = werknemersLijst.getSelectionModel().getSelectedItem();
        werknemer = dataService.getWerknemerByNaam(geselecteerdeNaam);

        toonData();
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

    public void toonData() {
        if (werknemer == null) {
            naamLabel.setText("Geen werknemer geselecteerd");
            urenLabel.setText("");
            loonLabel.setText("");
            return;
        }

        werknemer.wijzigMaand(geselecteerdeMaand);

        naamLabel.setText("Naam: " + werknemer.getNaam());
        urenLabel.setText("Uren gewerkt: " + werknemer.getUren());
        loonLabel.setText("Loon: " + werknemer.getLoon());
    }


    private void configureMaandSelectieListener() {
        maandSelectie.getSelectionModel().selectedItemProperty().addListener((observable, oudeWaarde, nieuweWaarde) -> {
            if (nieuweWaarde != null) {
                geselecteerdeMaand = nieuweWaarde;
                toonData();
            }
        });
    }
}
