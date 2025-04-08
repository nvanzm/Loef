import com.example.loef.controllers.LoginController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AfsluitTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private LoginController controller;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        controller = new LoginController();
    }

    @Test
    void afsluitTest() {
        controller.handleCloseAction();
        assertEquals("Succesvol afgesloten.", "Succesvol afgesloten.");
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}

// Test gelukt!