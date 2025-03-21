package com.example.loef;

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
            FXMLLoader loader = new FXMLLoader(UrenApplication.class.getResource(fxmlFileName));
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
        System.exit(0);
    }

    public void handleLoginAction(ActionEvent event) {
        if (gebruikersnaamField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            feedbackLabel.setText("Vul alle velden in.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        } else if (gebruikersnaamField.getText().equals("noach") && passwordField.getText().equals("123")) {
            // Werknemer
            feedbackLabel.setStyle("-fx-text-fill: green;");
            feedbackLabel.setText("Logging in...");
            switchToView(event, "/com/example/loef/uren-screen.fxml");

//            splashscreen??
//            SplashScreen.main(null);
        } else if (gebruikersnaamField.getText().equals("admin") && passwordField.getText().equals("admin")) {
            // Werkgever
            feedbackLabel.setStyle("-fx-text-fill: green;");
            feedbackLabel.setText("Welkom, Admin!");
//            switchToView(event, "/com/example/loef/admin-dashboard.fxml");
        } else {
            feedbackLabel.setStyle("-fx-text-fill: red;");
            feedbackLabel.setText("Ongeldige gebruikersnaam of wachtwoord.");
        }
    }
}
