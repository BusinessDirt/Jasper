package github.businessdirt.jasper.config.render.javafx;

import github.businessdirt.jasper.config.dummies.DummyConfig;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class JavaFXConfigRendererTest {

    @BeforeAll
    static void initJfxRuntime() {
        // Initialize JavaFX toolkit if not already initialized
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized, ignore
        }
    }

    @TempDir
    static Path tempDir;

    @Test
    void testRenderStringProperty() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                DummyConfig config = new DummyConfig(tempDir.resolve("dummy_config.json"));
                config.initialize();

                JavaFXConfigRenderer<DummyConfig> renderer = new JavaFXConfigRenderer<>();
                Parent renderedUi = renderer.render(config, "");

                assertNotNull(renderedUi);
                assertInstanceOf(VBox.class, renderedUi);

                VBox root = (VBox) renderedUi;
                assertEquals(5, root.getChildren().size()); // Expect one HBox for the property

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                latch.countDown();
            }
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS)); // Wait for the JavaFX thread to complete
    }


    public static class TestApplication extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {

            DummyConfig config = new DummyConfig(tempDir.resolve("dummy_config.json"));

            // Instantiate the renderer and get the UI
            JavaFXConfigRenderer<DummyConfig> renderer = new JavaFXConfigRenderer<>();
            Parent configUi = renderer.render(config, "");

            // Create a scene and set it on the primary stage
            Scene scene = new Scene(configUi, 400, 300);
            primaryStage.setTitle("Jasper Configuration");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }
}
