package com.example.loef.models;

import static com.example.loef.util.JsonService.leesJsonBestand;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Subklasse van Persoon die uren en loon verwerkt.
 */
public class Werknemer extends Persoon {

    private List<Double> uren = new ArrayList<>();
    private double loon = 13.0;
    private String bestandsPad;

    public Werknemer(String naam) {
        super(naam); // naam komt nu uit Persoon
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

    public double getUren() {
        return uren.stream().mapToDouble(Double::doubleValue).sum();
    }

    @Override
    public double berekenLoon() {
        return loon * getUren(); // method override van Persoon
    }

    public double getLoon() {
        return berekenLoon();
    }

    public void setLoon(double loon) {
        this.loon = loon;
    }
}
