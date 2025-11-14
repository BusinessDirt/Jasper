package github.businessdirt.jasper.config;

import github.businessdirt.jasper.config.data.Category;
import github.businessdirt.jasper.config.dummies.DummyConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfigHandlerTest {

    @TempDir
    Path tempDir;

    private Path configPath;
    private DummyConfig config;

    @BeforeEach
    void setUp() {
        configPath = tempDir.resolve("dummy_config.json");
        config = new DummyConfig(configPath);
    }

    @Test
    void testInitializationAndDefaultValues() {
        assertEquals(5, config.getProperties().size());
        assertFalse(config.booleanProperty);
        assertEquals("default", config.stringProperty);
        assertEquals(50, config.integerProperty);
        assertEquals("A", config.selectorProperty);
        assertEquals("secret", config.hiddenProperty);
    }

    @Test
    void testWriteAndReadData() throws IOException {
        // Change values
        config.booleanProperty = true;
        config.stringProperty = "new value";
        config.integerProperty = 75;

        // Mark dirty and write
        config.markDirty();
        config.writeData();

        assertTrue(Files.exists(configPath));

        // Create a new instance to read the data
        DummyConfig newConfig = new DummyConfig(configPath);
        newConfig.initialize(); // This will call readData

        // Assert values are loaded from file
        assertTrue(newConfig.booleanProperty);
        assertEquals("new value", newConfig.stringProperty);
        // This will fail if the bug in PropertyData.setValue is not fixed.
        // Gson reads numbers as Double, and setting an int field with a Double will throw IllegalArgumentException.
        assertEquals(75, newConfig.integerProperty);
        assertEquals("A", newConfig.selectorProperty); // This one was not changed
    }

    @Test
    void testReadDataWithPreExistingFile() throws IOException {
        String json = """
                {
                  "General": {
                    "Sub": {
                      "stringProperty": "from file"
                    },
                    "booleanProperty": true
                  },
                  "Other": {
                    "integerProperty": 25.0
                  }
                }""";
        Files.writeString(configPath, json);

        config.initialize();

        assertTrue(config.booleanProperty);
        assertEquals("from file", config.stringProperty);
        // This will fail if the bug in PropertyData.setValue is not fixed.
        assertEquals(25, config.integerProperty);
    }

    @Test
    void testGetCategories() {
        List<Category> categories = config.getCategories();

        assertEquals(2, categories.size());

        Category general = categories.stream().filter(c -> c.name().equals("General")).findFirst().orElse(null);
        assertNotNull(general);
        assertEquals(2, general.items().size());
        assertEquals("booleanProperty", general.items().get(0).property().name());
        assertEquals("stringProperty", general.items().get(1).property().name()); // subcategory sorting

        Category other = categories.stream().filter(c -> c.name().equals("Other")).findFirst().orElse(null);
        assertNotNull(other);
        assertEquals(2, other.items().size());
        assertEquals("integerProperty", other.items().get(0).property().name());
        assertEquals("selectorProperty", other.items().get(1).property().name());

        // Hidden property should not be in categories
        assertTrue(categories.stream().noneMatch(c -> c.name().equals("Hidden")));
    }

    @Test
    void testMarkDirty() throws IOException {
        config.writeData(); // should do nothing as not dirty
        assertFalse(Files.exists(configPath));

        config.markDirty();
        config.writeData(); // should write file now
        assertTrue(Files.exists(configPath));

        // Re-initialize to ensure dirty flag is reset after writeData
        config = new DummyConfig(configPath);
        config.initialize();

        // Ensure subsequent writeData does nothing if not dirty
        long sizeBefore = Files.size(configPath);
        config.writeData();
        long sizeAfter = Files.size(configPath);
        assertEquals(sizeBefore, sizeAfter); // File size should not change if not dirty
    }
}
