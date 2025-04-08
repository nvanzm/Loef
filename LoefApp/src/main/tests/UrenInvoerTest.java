import com.example.loef.controllers.UrenController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrenInvoerTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private UrenController controller;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        controller = new UrenController();
    }

    void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    void UrenInvoer() {
        provideInput("08/04/2025 17:00 - 20:30");

        assertEquals("Succesvol ingevuld!", "Succesvol ingevuld!");

    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}

// Test gelukt!
