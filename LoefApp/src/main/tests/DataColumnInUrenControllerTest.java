import com.example.loef.controllers.UrenController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class DataColumnInUrenControllerTest {

    private UrenController controller;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        controller = new UrenController();
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void tesdataColumnIsNull() {
        String message = "Cannot invoke \"javafx.scene.control.TableColumn.setCellValueFactory(javafx.util.Callback)\" because \"this.dataColumn\" is null";

        Exception exception = assertThrows(NullPointerException.class, () -> {
            controller.initialize();
        });
        assertEquals(message, exception.getMessage());
    }
}