package github.businessdirt.jasper.config.file;

import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileConfigTest {

    @TempDir
    Path tempDir;

    private Path configPath;
    private FileConfig fileConfig;

    @BeforeEach
    void setUp() {
        configPath = tempDir.resolve("test_config.json");
        fileConfig = new FileConfig(configPath);
    }

    @Test
    void testSaveAndLoad() throws IOException {
        fileConfig.set("database.host", "localhost");
        fileConfig.set("database.port", 5432);
        fileConfig.set("features.featureA.enabled", true);
        fileConfig.save();

        assertTrue(Files.exists(configPath));

        FileConfig newConfig = new FileConfig(configPath);
        newConfig.load();

        assertEquals("localhost", newConfig.get("database.host"));
        // Gson deserializes all numbers as Double
        assertEquals(5432.0, newConfig.get("database.port"));
        assertEquals(true, newConfig.get("features.featureA.enabled"));
        assertEquals("{port=5432.0, host=localhost}", newConfig.get("database").toString());
    }

    @Test
    void testGetWithDefault() throws IOException {
        fileConfig.load(); // Load an empty/non-existent file
        assertEquals("default", fileConfig.get("non.existent.key", "default"));
        assertNull(fileConfig.get("non.existent.key"));
        assertEquals("default", fileConfig.get("a.b.c", "default"));

        fileConfig.set("a.b.c", "value");
        assertEquals("value", fileConfig.get("a.b.c", "default"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetAll() throws IOException {
        fileConfig.set("a.x", 1);
        fileConfig.set("b.y", "two");
        fileConfig.save();

        Map<String, Object> all = fileConfig.getAll();
        assertEquals(2, all.size());

        Map<String, Object> a = (Map<String, Object>) all.get("a");
        assertEquals(1, a.get("x"));

        Map<String, Object> b = (Map<String, Object>) all.get("b");
        assertEquals("two", b.get("y"));

        assertThrows(UnsupportedOperationException.class, () -> all.put("c", 3));
    }

    @Test
    void testLoadNonExistentFile() throws IOException {
        assertDoesNotThrow(() -> {
            FileConfig config = new FileConfig(tempDir.resolve("non_existent.json"));
            config.load();
            assertTrue(config.getAll().isEmpty());
        });
    }

    @Test
    void testLoadMalformedJson() throws IOException {
        Files.writeString(configPath, "{ not a valid json }");
        assertThrows(JsonSyntaxException.class, () -> fileConfig.load());
    }

    @Test
    void testSetAndGet() {
        fileConfig.set("a.b.c", "value1");
        assertEquals("value1", fileConfig.get("a.b.c"));

        fileConfig.set("a.b.c", "value2");
        assertEquals("value2", fileConfig.get("a.b.c"));

        fileConfig.set("a.b.d", "value3");
        assertEquals("value2", fileConfig.get("a.b.c"));
        assertEquals("value3", fileConfig.get("a.b.d"));

        fileConfig.set("x", "value4");
        assertEquals("value4", fileConfig.get("x"));
    }

    @Test
    void testOverwrite() {
        fileConfig.set("a.b", "not a map");
        assertEquals("not a map", fileConfig.get("a.b"));

        fileConfig.set("a.b.c", "value");
        assertEquals("value", fileConfig.get("a.b.c"));
        assertEquals("{c=value}", fileConfig.get("a.b").toString());
    }
}