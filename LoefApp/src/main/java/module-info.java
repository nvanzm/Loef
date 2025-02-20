module com.example.loonberekening {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.compiler;
    requires org.json;


    opens com.example.loonberekening to javafx.fxml;
    exports com.example.loonberekening;
}