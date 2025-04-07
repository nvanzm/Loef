package com.example.loef.util;

import com.example.loef.controllers.UrenController;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonService {
     public static void saveJsonData(String sleutel, Object waarde, String maand) {
        String bestandsPad = Paths.get(UrenController.MAP_NAAM, maand + ".json").toString();
        JSONObject jsonObject = leesJsonBestand(bestandsPad);
        JSONArray jsonArray = jsonObject.optJSONArray(sleutel);
        if (jsonArray == null) jsonArray = new JSONArray();

        jsonArray.put(waarde);
        jsonObject.put(sleutel, jsonArray);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bestandsPad))) {
            writer.write(jsonObject.toString(4));
        } catch (IOException e) {
            System.err.println("Fout bij opslaan: " + e.getMessage());
        }
    }

     public static JSONObject leesJsonBestand(String bestandspad) {
        try {
            String inhoud = new String(Files.readAllBytes(Paths.get(bestandspad)));
            return new JSONObject(inhoud.isEmpty() ? "{}" : inhoud);
        } catch (IOException e) {
            System.err.println("Fout bij laden van JSON-bestand: " + e.getMessage());
            return new JSONObject();
        }
    }
}
