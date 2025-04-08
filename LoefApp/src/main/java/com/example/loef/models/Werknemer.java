package com.example.loef.models;

import static com.example.loef.util.JsonService.leesJsonBestand;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Werknemer {
    private String naam;
    private List<Double> uren = new ArrayList<>();
    private double loon = 13.0;
    private String bestandsPad;

    public Werknemer(String naam) {
        this.naam = naam;
        this.bestandsPad = "maandenData/Maart.json";
        laadUrenUitJson();
    }

    private void laadUrenUitJson() {
        uren.clear();

        JSONObject jsonObject = leesJsonBestand(bestandsPad);
        if (jsonObject == null) return;

        JSONArray urenArray = jsonObject.getJSONArray("uren");

        for (int i = 0; i < urenArray.length(); i++) {
            uren.add(urenArray.getDouble(i));
        }
    }

    public void wijzigMaand(String maand) {
        this.bestandsPad = "maandenData/" + maand + ".json";
        laadUrenUitJson();
    }

    public String getNaam() {
        return naam;
    }

    public double getUren() {
        return uren.stream().mapToDouble(Double::doubleValue).sum();
    }

    public double getLoon() {
        return loon * getUren();
    }

    public void setLoon(double loon) {
        this.loon = loon;
    }
}

