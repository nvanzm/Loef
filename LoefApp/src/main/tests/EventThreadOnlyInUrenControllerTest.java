import com.example.loef.controllers.UrenController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class EventThreadOnlyInUrenControllerTest {

    private UrenController controller;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        controller = new UrenController();
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void testEventThreadOnly() {
        String message = "This operation is permitted on the event thread only; currentThread = main";

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            controller.exportExcel();
        });
        assertEquals(message, exception.getMessage());
    }
}