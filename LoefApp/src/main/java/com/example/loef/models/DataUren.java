package com.example.loef.models;

import com.example.loef.util.JsonService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataUren {

    private static final String MAP_NAAM = "maandenData/";
    private final ObservableList<DataUrenItem> dataLijst = FXCollections.observableArrayList();

    public ObservableList<DataUrenItem> getDataLijst() {
        return dataLijst;
    }

    public void laadData(String maand, Werknemer werknemer) {
        dataLijst.clear();
        File jsonBestand = new File(MAP_NAAM + maand + ".json");

        if (!jsonBestand.exists()) return;

        try {
            String inhoud = new String(Files.readAllBytes(Paths.get(jsonBestand.getPath())));
            JSONObject jsonObject = new JSONObject(inhoud);

            JSONArray dataArray = jsonObject.optJSONArray("data");
            JSONArray urenArray = jsonObject.optJSONArray("uren");

            if (dataArray != null && urenArray != null) {
                for (int i = 0; i < dataArray.length(); i++) {
                    String data = dataArray.getString(i);
                    double uren = urenArray.getDouble(i);
                    dataLijst.add(new DataUrenItem(data, uren, werknemer));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void voegToe(String data, double uren, String maand, Werknemer werknemer) {
        dataLijst.add(new DataUrenItem(data, uren, werknemer));
        slaOp(maand);
    }

    public void verwijder(DataUrenItem item, String maand) {
        dataLijst.remove(item);
        slaOp(maand);
    }

    public void slaOp(String maand) {
        JSONArray dataArray = new JSONArray();
        JSONArray urenArray = new JSONArray();

        for (DataUrenItem record : dataLijst) {
            dataArray.put(record.getData());
            urenArray.put(record.getUren());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", dataArray);
        jsonObject.put("uren", urenArray);

        JsonService.schrijfHeleJson(MAP_NAAM + maand + ".json", jsonObject);
    }

    public double getTotaalUren() {
        return dataLijst.stream().mapToDouble(DataUrenItem::getUren).sum();
    }

    public double getTotaalLoon(double uurloon) {
        return getTotaalUren() * uurloon;
    }
}
