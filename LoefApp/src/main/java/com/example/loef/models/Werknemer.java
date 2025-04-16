package com.example.loef.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.loef.util.JsonService.leesJsonBestand;

public class Werknemer extends Persoon {
    private final List<DataUrenItem> dataUrenItems = new ArrayList<>();
    private double uurloon = 13.0;
    private String bestandsPad;

    public Werknemer(String naam) {
        super(naam);
        this.bestandsPad = "maandenData/April.json";
    }

    public void voegUrenItemToe(DataUrenItem item) {
        dataUrenItems.add(item);
    }

    public List<DataUrenItem> getDataUrenItems() {
        return dataUrenItems;
    }

    public void wijzigMaand(String maand) {
        this.bestandsPad = "maandenData/" + maand + ".json";
        laadUrenUitJson();
    }

    private void laadUrenUitJson() {
        dataUrenItems.clear();
        JSONObject jsonObject = leesJsonBestand(bestandsPad);
        if (jsonObject == null) return;

        JSONArray dataArray = jsonObject.optJSONArray("data");
        JSONArray urenArray = jsonObject.optJSONArray("uren");

        if (dataArray != null && urenArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                String data = dataArray.getString(i);
                double uren = urenArray.getDouble(i);
                new DataUrenItem(data, uren, this);
            }
        }
    }

    public double getUren() {
        return dataUrenItems.stream().mapToDouble(DataUrenItem::getUren).sum();
    }

    public double getLoon() {
        return uurloon * getUren();
    }

    public void setUurloon(double uurloon) {
        this.uurloon = uurloon;
    }

    @Override
    public double berekenLoon() {
        return getLoon();
    }
}
