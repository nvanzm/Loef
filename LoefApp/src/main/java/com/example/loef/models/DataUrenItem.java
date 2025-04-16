package com.example.loef.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataUrenItem {
    private final String data;
    private final double uren;
    private final Werknemer werknemer;

    public DataUrenItem(String data, double uren, Werknemer werknemer) {
        this.data = data;
        this.uren = uren;
        this.werknemer = werknemer;

        if (werknemer != null) {
            werknemer.voegUrenItemToe(this);
        }
    }

    public String getData() {
        return data;
    }

    public double getUren() {
        return uren;
    }

    public Werknemer getWerknemer() {
        return werknemer;
    }

    public String getDatumZonderTijd() {
        if (data.contains(" - Tijd:")) {
            return data.split(" - Tijd:")[0];
        }
        return data;
    }

    public String getTijdRange() {
        if (data.contains("Tijd:")) {
            return data.substring(data.indexOf("Tijd:") + 5).trim();
        }
        return "";
    }

    public String getWerknemerNaam() {
        return werknemer != null ? werknemer.getNaam() : "Onbekend";
    }

    public LocalDate getDatumAlsLocalDate() {
        try {
            String datumStr = getDatumZonderTijd();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(datumStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return data + " (" + uren + " uur) - Werknemer: " + getWerknemerNaam();
    }
}
