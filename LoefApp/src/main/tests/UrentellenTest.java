import com.example.loef.controllers.UrenController;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UrentellenTest {

    @BeforeAll
    static void initToolkit() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @Test
    void testTelUren() throws Exception {
        UrenController controller = new UrenController();
        controller.infoInput = new TextField("02/04/2025 17:00 - 20:30");
        controller.substringOutput = new Label();

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.telUren();
            latch.countDown();
        });

        latch.await();

        assertEquals("3.5 uren toegevoegd!", controller.substringOutput.getText());
    }
}

// Test gelukt!