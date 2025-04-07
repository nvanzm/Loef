package com.example.loef.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WerknemerData {

    private static WerknemerData instance;
    private ObservableList<Werknemer> werknemers = FXCollections.observableArrayList();

    private WerknemerData() {
        werknemers.add(new Werknemer("Noach Ambachtsheer"));
    }

    public static WerknemerData getInstance() {
        if (instance == null) {
            instance = new WerknemerData();
        }
        return instance;
    }

    public ObservableList<Werknemer> getWerknemers() {
        return werknemers;
    }

    public Werknemer getWerknemerByNaam(String naam) {
        for (Werknemer werknemer : werknemers) {
            if (werknemer.getNaam().equalsIgnoreCase(naam)) {
                return werknemer;
            }
        }
        return null;
    }
}
