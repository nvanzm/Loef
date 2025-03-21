package com.example.loef;

import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LoginController {

    @FXML
    private VBox loginContainer;

    @FXML
    public void initialize() {
        //Styling
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(30);
        dropShadow.setColor(Color.BLACK);
        loginContainer.setEffect(dropShadow);
    }
}

