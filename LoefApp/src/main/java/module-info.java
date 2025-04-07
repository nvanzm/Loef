module com.example.loef {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.compiler;
    requires org.json;
    requires java.prefs;
    requires org.apache.poi.ooxml;


    opens com.example.loef to javafx.fxml;
    exports com.example.loef;
    exports com.example.loef.controllers;
    opens com.example.loef.controllers to javafx.fxml;
    exports com.example.loef.models;
    opens com.example.loef.models to javafx.fxml;
}