package com.example.loonberekening;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelloController {
    private static final int uurloon = 13;
    private static final String CONFIG_FILE = "urenLijst.json";
    private static final String DATA_FILE = "dataLijst.json";

    @FXML private TextField infoInput;
    @FXML private Label substringOutput, urenOutput, geldOutput;
    @FXML private TableView<DataUren> dataListTable;
    @FXML private TableColumn<DataUren, String> dataColumn, urenColumn;

    private final ObservableList<DataUren> dataObservableList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData()));
        urenColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUren())));

        dataListTable.setItems(dataObservableList);

        toonData();
    }

    private void toonData() {
        try {
            String contentData = new String(Files.readAllBytes(Paths.get(DATA_FILE)));
            String contentUren = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));

            if (!contentData.trim().isEmpty()) {
                JSONObject dataJson = new JSONObject(contentData);
                JSONObject urenJson = new JSONObject(contentUren);
                JSONArray dataArray = dataJson.optJSONArray("data");
                JSONArray urenArray = urenJson.optJSONArray("uren");

                dataObservableList.clear();

                if (dataArray != null && urenArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        String item = dataArray.getString(i);
                        double uren = urenArray.getDouble(i);
                        dataObservableList.add(new DataUren(item, uren));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Fout bij het laden van data: " + e.getMessage());
        }
    }

    public static class DataUren {
        private String data;
        private double uren;

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
    }

    public void telUren() {
        String eersteUur, tweedeUur, tweedeUurFirstString, eersteUurFirstString, tweedeUurSecondString, eersteUurSecondString;
        double tweedeUrenFirstDouble, eersteUrenFirstDouble, tweedeUrenSecondDouble, eersteUrenSecondDouble, totalWork;

        if (!infoInput.getText().isEmpty()) {
            String infoInputText = infoInput.getText();
            String aangepasteText = infoInputText.substring(11);

            eersteUur = aangepasteText.substring(0, 5);
            tweedeUur = aangepasteText.substring(8);

            tweedeUurFirstString = tweedeUur.substring(0, 2);
            tweedeUurSecondString = tweedeUur.substring(3);

            eersteUurFirstString = eersteUur.substring(0, 2);
            eersteUurSecondString = eersteUur.substring(3);

            tweedeUrenFirstDouble = Double.parseDouble(tweedeUurFirstString);
            eersteUrenFirstDouble = Double.parseDouble(eersteUurFirstString);
            tweedeUrenSecondDouble = Double.parseDouble(tweedeUurSecondString);
            eersteUrenSecondDouble = Double.parseDouble(eersteUurSecondString);

            double tweedeUurInDecimalen = tweedeUrenFirstDouble + (tweedeUrenSecondDouble / 60.0);
            double eersteUurInDecimalen = eersteUrenFirstDouble + (eersteUrenSecondDouble / 60.0);

            totalWork = tweedeUurInDecimalen - eersteUurInDecimalen;

            urenOutput.setText("Gewerkte uren: " + totalWork);
            saveUren(totalWork);
            saveData(infoInput.getText());
        } else {
            substringOutput.setText("Voer een reeks in!");
        }
    }

    @FXML
    public void urenLijst() {
        double totaalUren = 0;

        try {
            String content = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));

            if (!content.trim().isEmpty()) {
                JSONObject urenJson = new JSONObject(content);
                JSONArray urenArray = urenJson.optJSONArray("uren");

                if (urenArray != null) {
                    for (int i = 0; i < urenArray.length(); i++) {
                        totaalUren += urenArray.getDouble(i);
                    }
                }
            }
        } catch (IOException e) {
            urenOutput.setText("Er zijn nog geen uren opgeslagen.");
        }

        double totaalVerdient = totaalUren * uurloon;
        geldOutput.setText("Totaal verdiend: €" + totaalVerdient);
        urenOutput.setText("Totale uren: " + totaalUren);
    }

    private void saveUren(double totaleUren) {
        saveJsonData(CONFIG_FILE, "uren", totaleUren);
        initialize();
    }

    private void saveData(String data) {
        saveJsonData(DATA_FILE, "data", data);
        initialize();
    }

    private void saveJsonData(String bestand, String sleutel, Object waarde) {
        try {
            JSONObject jsonObject = leesJsonBestand(bestand);
            JSONArray jsonArray = jsonObject.optJSONArray(sleutel);
            if (jsonArray == null) jsonArray = new JSONArray();

            jsonArray.put(waarde);
            jsonObject.put(sleutel, jsonArray);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(bestand))) {
                writer.write(jsonObject.toString(4));
            }
        } catch (IOException e) {
            System.err.println("Fout bij opslaan: " + e.getMessage());
        }
    }

    private JSONObject leesJsonBestand(String pad) {
        try {
            String inhoud = new String(Files.readAllBytes(Paths.get(pad)));
            return new JSONObject(inhoud.isEmpty() ? "{}" : inhoud);
        } catch (IOException e) {
            return new JSONObject();
        }
    }
}
