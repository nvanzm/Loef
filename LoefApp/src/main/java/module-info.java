module com.example.loef {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.compiler;
    requires org.json;
    requires java.prefs;


    opens com.example.loef to javafx.fxml;
    exports com.example.loef;
}