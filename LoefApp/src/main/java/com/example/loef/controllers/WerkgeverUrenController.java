package com.example.loef.controllers;

import com.example.loef.models.DataUren;
import com.example.loef.models.Werknemer;
import com.example.loef.models.WerknemerData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class WerkgeverUrenController {

    @FXML
    private Label naamLabel;

    @FXML
    private Label urenLabel;

    @FXML
    private Label loonLabel;

    @FXML
    private ListView<String> werknemersLijst;

    private final WerknemerData dataService = WerknemerData.getInstance();

    @FXML
    public void initialize() {
        for (Werknemer werknemer : dataService.getWerknemers()) {
            werknemersLijst.getItems().add(werknemer.getNaam());
        }
    }

    @FXML
    private void selecteerWerknemer(MouseEvent event) {
        String geselecteerdeNaam = werknemersLijst.getSelectionModel().getSelectedItem();
        Werknemer werknemer = dataService.getWerknemerByNaam(geselecteerdeNaam);

        if (werknemer != null) {
            naamLabel.setText("Naam: " + werknemer.getNaam());
            urenLabel.setText("Uren gewerkt: " + werknemer.getUren());
            loonLabel.setText("Loon: " + werknemer.getLoon());
        } else {
            urenLabel.setText("Geen werknemer geselecteerd");
            loonLabel.setText("");
        }
    }
}
