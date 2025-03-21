package com.example.loef;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UrenController {
    private static final int UURLOON = 13;
    private static final String MAP_NAAM = "maandenData/";
    private String geselecteerdeMaand = "Maart";

    @FXML
    private TextField infoInput;
    @FXML
    private Label substringOutput, urenOutput, geldOutput;
    @FXML
    private TableView<DataUren> dataListTable;
    @FXML
    private TableColumn<DataUren, String> dataColumn, urenColumn;
    @FXML
    private ComboBox<String> maandSelectie;
    @FXML
    private HBox mainHBox;

    private final ResolutionController resolutionManager = ResolutionController.getInstance();

    private final ObservableList<DataUren> dataObservableList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        applyResolution(resolutionManager.getCurrentResolution());

        dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData()));
        urenColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUren())));
        dataListTable.setItems(dataObservableList);

        maandSelectie.getSelectionModel().selectedItemProperty().addListener((observable, oudeWaarde, nieuweWaarde) -> {
            if (nieuweWaarde != null) {
                geselecteerdeMaand = nieuweWaarde;
                toonData();
            }
        });

        updateUrenEnLoon();
        toonData();
    }

    private void applyResolution(String resolution) {
        String[] dimensions = resolution.split("x");
        if (dimensions.length == 2) {
            try {
                double width = Double.parseDouble(dimensions[0]);
                double height = Double.parseDouble(dimensions[1]);
                mainHBox.setPrefWidth(width);
                mainHBox.setPrefHeight(height);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void toonData() {
        String bestandsPad = Paths.get(MAP_NAAM, geselecteerdeMaand + ".json").toString();
        try {
            String inhoud = new String(Files.readAllBytes(Paths.get(bestandsPad)));
            if (!inhoud.trim().isEmpty()) {
                JSONObject json = new JSONObject(inhoud);
                JSONArray dataArray = json.optJSONArray("data");
                JSONArray urenArray = json.optJSONArray("uren");

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
            System.out.println("Geen bestaande data voor deze maand.");
            dataObservableList.clear();
        }

        updateUrenEnLoon();
    }

    @FXML
    public void telUren() {
        if (!infoInput.getText().isEmpty()) {
            String infoInputText = infoInput.getText();
            String aangepasteText = infoInputText.substring(11);

            String eersteUur = aangepasteText.substring(0, 5);
            String tweedeUur = aangepasteText.substring(8);

            String tweedeUurFirstString = tweedeUur.substring(0, 2);
            String tweedeUurSecondString = tweedeUur.substring(3);

            String eersteUurFirstString = eersteUur.substring(0, 2);
            String eersteUurSecondString = eersteUur.substring(3);

            double tweedeUrenFirstDouble = Double.parseDouble(tweedeUurFirstString);
            double eersteUrenFirstDouble = Double.parseDouble(eersteUurFirstString);
            double tweedeUrenSecondDouble = Double.parseDouble(tweedeUurSecondString);
            double eersteUrenSecondDouble = Double.parseDouble(eersteUurSecondString);

            double tweedeUurInDecimalen = tweedeUrenFirstDouble + (tweedeUrenSecondDouble / 60.0);
            double eersteUurInDecimalen = eersteUrenFirstDouble + (eersteUrenSecondDouble / 60.0);

            double totalWork = tweedeUurInDecimalen - eersteUurInDecimalen;

            saveUren(totalWork);
            saveData(infoInput.getText());
            updateUrenEnLoon();
        } else {
            substringOutput.setText("Voer een reeks in!");
        }
    }

    private void updateUrenEnLoon() {
        double totaalUren = dataObservableList.stream().mapToDouble(DataUren::getUren).sum();
        double totaalVerdient = totaalUren * UURLOON;

        urenOutput.setText("Gewerkte uren: " + totaalUren);
        geldOutput.setText("Totaal verdiend: €" + String.format("%.2f", totaalVerdient));
    }

    private void saveUren(double totaleUren) {
        saveJsonData("uren", totaleUren);
        toonData();
    }

    private void saveData(String data) {
        saveJsonData("data", data);
        toonData();
    }

    private void saveJsonData(String sleutel, Object waarde) {
        String bestandsPad = Paths.get(MAP_NAAM, geselecteerdeMaand + ".json").toString();
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

    private JSONObject leesJsonBestand(String bestandspad) {
        try {
            String inhoud = new String(Files.readAllBytes(Paths.get(bestandspad)));
            return new JSONObject(inhoud.isEmpty() ? "{}" : inhoud);
        } catch (IOException e) {
            System.err.println("Fout bij laden van JSON-bestand: " + e.getMessage());
            return new JSONObject();
        }
    }

    public static class DataUren {
        private final String data;
        private final double uren;

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
}