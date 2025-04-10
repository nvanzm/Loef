import com.example.loef.controllers.UrenController;
import com.example.loef.models.DataUren;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateUrenTest {

    private UrenController controller;

    @BeforeAll
    static void initToolkit() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @BeforeEach
    void setUp() {
        controller = new UrenController();

        controller.dataObservableList.setAll(
                new DataUren("08/04/2025 17:00 - 20:30", 3.5)
        );

        controller.urenOutput = new Label();
        controller.geldOutput = new Label();
    }

    @Test
    void test() {
        controller.updateUrenEnLoon();

        assertEquals("Gewerkte uren: 3.5", controller.urenOutput.getText());
        assertEquals("Totaal verdiend: €45.50", controller.geldOutput.getText());
    }
}

// Test gelukt!