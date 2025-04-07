package com.example.loef.controllers;

import com.example.loef.models.DataUren;
import com.example.loef.models.WerknemerData;
import com.example.loef.util.JsonService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public static final String MAP_NAAM = "maandenData/";
    public String geselecteerdeMaand = "April";

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

    private final ObservableList<DataUren> dataObservableList = FXCollections.observableArrayList();
    private final ResolutionController resolutionManager = ResolutionController.getInstance();
    private final ExcelExporter excelExporter = new ExcelExporter();

    private final JsonService jsonService = new JsonService();

    @FXML
    public void initialize() {
        configureTableColumns();
        applyResolution(resolutionManager.getCurrentResolution());
        configureMaandSelectieListener();
        toonData();
        updateUrenEnLoon();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");

        deleteItem.setStyle(
                "-fx-background-color: #e74c3c; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10px 15px; " +
                        "-fx-border-radius: 3px; "
        );
        deleteItem.setOnAction(event -> deleteSelectedItem());
        contextMenu.getItems().add(deleteItem);

        dataListTable.setRowFactory(tableView -> {
            TableRow<DataUren> row = new TableRow<>();

            row.setContextMenu(contextMenu);

            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && !row.isEmpty()) {
                    DataUren selectedItem = row.getItem();
                }
            });

            return row;
        });
    }

    private void configureTableColumns() {
        dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData()));
        urenColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getUren())));
        dataListTable.setItems(dataObservableList);
    }

    private void configureMaandSelectieListener() {
        maandSelectie.getSelectionModel().selectedItemProperty().addListener((observable, oudeWaarde, nieuweWaarde) -> {
            if (nieuweWaarde != null) {
                geselecteerdeMaand = nieuweWaarde;
                toonData();
            }
        });
    }

    @FXML
    public void exportExcel() {
        String bestandsPad = Paths.get(MAP_NAAM, geselecteerdeMaand + ".json").toString();
        File bestand = new File(bestandsPad);

        if (!bestand.exists()) {
            showAlert(Alert.AlertType.ERROR, "Geen gegevens beschikbaar voor " + geselecteerdeMaand);
            return;
        }

        try {
            String inhoud = new String(Files.readAllBytes(Paths.get(bestandsPad)));
            JSONObject json = new JSONObject(inhoud);

            JSONArray dataArray = json.optJSONArray("data");
            JSONArray urenArray = json.optJSONArray("uren");

            if (dataArray != null && urenArray != null) {
                excelExporter.exportToExcel(geselecteerdeMaand, dataArray, urenArray);
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Fout bij het lezen van het JSON-bestand.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.showAndWait();
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

    public void updateUrenEnLoon() {
        double totaalUren = dataObservableList.stream().mapToDouble(DataUren::getUren).sum();
        double totaalVerdient = totaalUren * UURLOON;

        urenOutput.setText("Gewerkte uren: " + totaalUren);
        geldOutput.setText("Totaal verdiend: €" + String.format("%.2f", totaalVerdient));
    }

    private void saveUren(double totaleUren) {
        JsonService.saveJsonData("uren", totaleUren, geselecteerdeMaand);
        toonData();
    }

    private void saveData(String data) {
        JsonService.saveJsonData("data", data, geselecteerdeMaand);
        toonData();
    }

    private void deleteSelectedItem() {
        DataUren selectedItem = dataListTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            dataObservableList.remove(selectedItem);
            removeDataFromJson(selectedItem);
            updateUrenEnLoon();
        }
    }

    private void removeDataFromJson(DataUren selectedItem) {
        String bestandsPad = Paths.get(MAP_NAAM, geselecteerdeMaand + ".json").toString();
        JSONObject jsonObject = JsonService.leesJsonBestand(bestandsPad);

        JSONArray dataArray = jsonObject.optJSONArray("data");
        JSONArray urenArray = jsonObject.optJSONArray("uren");

        if (dataArray != null && urenArray != null) {
            int index = dataArray.toList().indexOf(selectedItem.getData());
            if (index != -1) {
                dataArray.remove(index);
                urenArray.remove(index);

                jsonObject.put("data", dataArray);
                jsonObject.put("uren", urenArray);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bestandsPad))) {
            writer.write(jsonObject.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ExcelExporter {

        public void exportToExcel(String maand, JSONArray dataArray, JSONArray urenArray) throws IOException {
            Workbook werkboek = new XSSFWorkbook();
            Sheet sheet = werkboek.createSheet(maand);

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Datum");
            headerRow.createCell(1).setCellValue("Uren");

            for (int i = 0; i < dataArray.length(); i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(dataArray.getString(i));
                row.createCell(1).setCellValue(urenArray.getDouble(i));
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel bestand", "*.xlsx"));
            fileChooser.setInitialFileName(maand + ".xlsx");

            File gekozenBestand = fileChooser.showSaveDialog(null);
            if (gekozenBestand != null) {
                try (FileOutputStream fileOut = new FileOutputStream(gekozenBestand)) {
                    werkboek.write(fileOut);
                    werkboek.close();

                    new Alert(Alert.AlertType.INFORMATION, "Bestand succesvol opgeslagen als Excel!", ButtonType.OK).showAndWait();
                }
            }
        }
    }
}
