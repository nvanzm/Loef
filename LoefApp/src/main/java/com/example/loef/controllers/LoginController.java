package com.example.loef.controllers;

import com.example.loef.models.Login;
import com.example.loef.Startup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField gebruikersnaamField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label feedbackLabel;

    @FXML
    private VBox loginContainer;

    private static void switchToView(ActionEvent event, String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(Startup.class.getResource(fxmlFileName));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            stage.setTitle("Loef");
            stage.setMaximized(true);

        } catch (IOException e) {
            System.err.println("Fout bij het laden van de view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Styling
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(30);
        dropShadow.setColor(Color.BLACK);
        loginContainer.setEffect(dropShadow);
    }

    public void handleCloseAction() {
        System.out.println("Succesvol afgesloten.");
        System.exit(0);
    }

    public void handleLoginAction(ActionEvent event) {
        Login login = new Login(gebruikersnaamField.getText(), passwordField.getText());

        String feedback = login.validate();

        if (feedback.equals("Logging in...")) {
            feedbackLabel.setStyle("-fx-text-fill: green;");
            switchToView(event, "/com/example/loef/werknemer/werknemer-uren-screen.fxml");
        } else if (feedback.equals("Welkom, Admin!")) {
            feedbackLabel.setStyle("-fx-text-fill: green;");
            switchToView(event, "/com/example/loef/werkgever/werkgever-uren-screen.fxml");
        } else {
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
        System.out.println(feedback);
    }

}
