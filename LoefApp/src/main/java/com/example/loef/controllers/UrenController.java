package com.example.loef.controllers;

import com.example.loef.models.DataUrenItem;
import com.example.loef.models.DataUren;
import com.example.loef.models.Werknemer;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UrenController {

    private static final int UURLOON = 13;
    private final Werknemer actieveWerknemer = new Werknemer("Noach Ambachtsheer");
    public static final String MAP_NAAM = "maandenData/";

    private final DataUren dataModel = new DataUren();
    public static String geselecteerdeMaand = "April";

    @FXML private TextField infoInput;
    @FXML private Label substringOutput, urenOutput, geldOutput;
    @FXML private TableView<DataUrenItem> dataListTable;
    @FXML private TableColumn<DataUrenItem, String> dataColumn, urenColumn;

    @FXML private ComboBox<String> maandSelectie;
    @FXML private HBox mainHBox;

    private final ResolutionController resolutionManager = ResolutionController.getInstance();

    @FXML
    public void initialize() {
        configureTableColumns();
        applyResolution(resolutionManager.getCurrentResolution());
        configureMaandSelectie();
        configureContextMenu();

        maandSelectie.getItems().addAll(
                "Januari", "Februari", "Maart", "April", "Mei", "Juni",
                "Juli", "Augustus", "September", "Oktober", "November", "December"
        );
        maandSelectie.getSelectionModel().select(geselecteerdeMaand);
        laadData();
    }

    private void configureTableColumns() {
        dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData()));
        urenColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUren())));
        dataListTable.setItems(dataModel.getDataLijst());
    }

    private void configureMaandSelectie() {
        maandSelectie.getSelectionModel().selectedItemProperty().addListener((obs, oud, nieuw) -> {
            if (nieuw != null) {
                geselecteerdeMaand = nieuw;
                laadData();
            }
        });
    }

    private void configureContextMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem verwijderItem = new MenuItem("Verwijder");
        verwijderItem.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        verwijderItem.setOnAction(e -> verwijderGeselecteerdeItem());
        menu.getItems().add(verwijderItem);

        dataListTable.setRowFactory(tv -> {
            TableRow<DataUrenItem> row = new TableRow<>();
            row.setContextMenu(menu);
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && !row.isEmpty()) {

                }
            });
            return row;
        });
    }

    private void applyResolution(String resolution) {
        String[] parts = resolution.split("x");
        if (parts.length == 2) {
            try {
                double width = Double.parseDouble(parts[0]);
                double height = Double.parseDouble(parts[1]);
                mainHBox.setPrefWidth(width);
                mainHBox.setPrefHeight(height);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void laadData() {
        dataModel.laadData(geselecteerdeMaand, actieveWerknemer);
        updateUrenEnLoon();
    }

    @FXML
    public void telUren() {
        String input = infoInput.getText();
        if (input.isEmpty()) {
            substringOutput.setText("Voer een tijdregel in!");
            return;
        }

        try {
            String tijdData = input.substring(11);
            String beginTijd = tijdData.substring(0, 5);
            String eindTijd = tijdData.substring(8);

            double gewerkteUren = berekenUren(beginTijd, eindTijd);

            dataModel.voegToe(input, gewerkteUren, geselecteerdeMaand, actieveWerknemer);

            updateUrenEnLoon();

            infoInput.clear();
            substringOutput.setText(gewerkteUren + " uur toegevoegd!");
        } catch (Exception e) {
            substringOutput.setText("Ongeldige invoer! Voorbeeld: 15-04-2025 - Tijd: 14:00 tot 18:00");
        }
    }

    private double berekenUren(String begin, String eind) {
        int startUur = Integer.parseInt(begin.substring(0, 2));
        int startMin = Integer.parseInt(begin.substring(3));
        int eindUur = Integer.parseInt(eind.substring(0, 2));
        int eindMin = Integer.parseInt(eind.substring(3));
        return (eindUur + eindMin / 60.0) - (startUur + startMin / 60.0);
    }

    private void updateUrenEnLoon() {
        double totaalUren = dataModel.getTotaalUren();
        double totaalLoon = dataModel.getTotaalLoon(UURLOON);

        urenOutput.setText("Gewerkte uren: " + totaalUren);
        geldOutput.setText("Totaal verdiend: €" + String.format("%.2f", totaalLoon));
    }

    private void verwijderGeselecteerdeItem() {
        DataUrenItem geselecteerd = dataListTable.getSelectionModel().getSelectedItem();

        if (geselecteerd != null) {
            dataModel.verwijder(geselecteerd, geselecteerdeMaand);
            updateUrenEnLoon();
        }
    }

    @FXML
    public void exportExcel() {
        String bestandPad = MAP_NAAM + geselecteerdeMaand + ".json";
        File bestand = new File(bestandPad);

        if (!bestand.exists()) {
            toonAlert(Alert.AlertType.ERROR, "Geen gegevens beschikbaar voor " + geselecteerdeMaand);
            return;
        }

        try {
            String inhoud = new String(Files.readAllBytes(Paths.get(bestandPad)));
            JSONObject json = new JSONObject(inhoud);
            JSONArray data = json.optJSONArray("data");
            JSONArray uren = json.optJSONArray("uren");

            if (data != null && uren != null) {
                exportToExcel(geselecteerdeMaand, data, uren);
            }
        } catch (IOException e) {
            toonAlert(Alert.AlertType.ERROR, "Fout bij het lezen van JSON-bestand.");
            e.printStackTrace();
        }
    }

    private void exportToExcel(String maand, JSONArray data, JSONArray uren) throws IOException {
        Workbook werkboek = new XSSFWorkbook();
        Sheet sheet = werkboek.createSheet(maand);

        Row kop = sheet.createRow(0);
        kop.createCell(0).setCellValue("Datum");
        kop.createCell(1).setCellValue("Uren");

        for (int i = 0; i < data.length(); i++) {
            Row rij = sheet.createRow(i + 1);
            rij.createCell(0).setCellValue(data.getString(i));
            rij.createCell(1).setCellValue(uren.getDouble(i));
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(maand + ".xlsx");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel-bestand", "*.xlsx"));

        File gekozenBestand = fileChooser.showSaveDialog(null);
        if (gekozenBestand != null) {
            try (FileOutputStream out = new FileOutputStream(gekozenBestand)) {
                werkboek.write(out);
                werkboek.close();
                toonAlert(Alert.AlertType.INFORMATION, "Excel succesvol opgeslagen!");
            }
        }
    }

    private void toonAlert(Alert.AlertType type, String bericht) {
        new Alert(type, bericht, ButtonType.OK).showAndWait();
    }
}
