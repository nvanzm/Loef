package com.example.loef;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NavigationController {

    @FXML
    private AnchorPane scenePane;

    private void switchScene(ActionEvent event, String fxmlFileName) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFileName)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void werknemerRooster(ActionEvent event) throws IOException {
        switchScene(event, "werknemer/werknemer-rooster-screen.fxml");
    }

    public void werknemerUren(ActionEvent event) throws IOException {
        switchScene(event, "werknemer/werknemer-uren-screen.fxml");
    }

    public void werkgeverRooster(ActionEvent event) throws IOException {
        switchScene(event, "werkgever/werkgever-rooster-screen.fxml");
    }

    public void werkgeverUren(ActionEvent event) throws IOException {
        switchScene(event, "werkgever/werkgever-uren-screen.fxml");
    }

    public void werknemerSettings(ActionEvent event) throws IOException {
        switchScene(event, "werknemer/werknemer-settings-screen.fxml");
    }

    public void werkgeverSettings(ActionEvent event) throws IOException {
        switchScene(event, "werkgever/werkgever-settings-screen.fxml");
    }

    public void logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to logout");
        alert.setContentText("Are you sure you want to logout?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            Stage stage = (Stage) scenePane.getScene().getWindow();
            System.out.println("You've been logged out.");
            stage.close();
        }
    }

    public void line(ActionEvent event) throws IOException {

    }
}
