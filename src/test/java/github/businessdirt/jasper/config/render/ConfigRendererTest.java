package github.businessdirt.jasper.config.render;

import github.businessdirt.jasper.config.dummies.DummyConfig;
import github.businessdirt.jasper.config.dummies.DummyConfigRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigRendererTest {

    @TempDir
    Path tempDir;

    private DummyConfigRenderer renderer;
    private DummyConfig config;

    @BeforeEach
    void setUp() throws IOException {
        this.config = new DummyConfig(this.tempDir.resolve("dummy_config.json"));
        this.renderer = new DummyConfigRenderer();

        this.config.initialize();
    }

    @Test
    @DisplayName("Should render all visible properties.")
    void testRender() {
        String result = this.renderer.render(this.config, "");

        assertTrue(result.contains("General.booleanProperty"));
        assertTrue(result.contains("General.Sub.stringProperty"));
        assertTrue(result.contains("Other.integerProperty"));
        assertTrue(result.contains("Other.selectorProperty"));
        assertFalse(result.contains("Hidden.hiddenProperty"));
    }

    @Test
    @DisplayName("Should filter properties correctly with a search query.")
    void testRenderWithSearchQuery() {
        String result = this.renderer.render(this.config, "General");

        assertTrue(result.contains("General.booleanProperty"));
        assertTrue(result.contains("General.Sub.stringProperty"));
        assertFalse(result.contains("Other.integerProperty"));
        assertFalse(result.contains("Other.selectorProperty"));
        assertFalse(result.contains("Hidden.hiddenProperty"));
    }
}
