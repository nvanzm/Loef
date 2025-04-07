package com.example.loef.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataUren {
    private final String data;
    private final double uren;

    private final ObservableList<DataUren> dataObservableList = FXCollections.observableArrayList();

    public DataUren(String data, double uren) {
        this.data = data;
        this.uren = uren;
    }

    public String getData() {
        return data;
    }

    public double getUren() {
        return uren;
    }

    public String getWerknemerNaam() {
        if (data.contains(" - ")) {
            return data.split(" - ")[1];
        }
        return data;
    }

    @Override
    public String toString() {
        return data + " (" + uren + " uur)";
    }
}
