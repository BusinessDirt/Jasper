package github.businessdirt.jasper.config.file;

import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
        fileConfig.set(List.of("database", "host"), "localhost");
        fileConfig.set(List.of("database", "port"), 5432);
        fileConfig.set(List.of("features", "featureA", "enabled"), true);
        fileConfig.save();

        assertTrue(Files.exists(configPath));

        FileConfig newConfig = new FileConfig(configPath);
        newConfig.load();

        assertEquals("localhost", newConfig.get(List.of("database", "host")));

        // Gson deserializes all numbers as Double
        assertEquals(5432.0, newConfig.get(List.of("database", "port")));
        assertEquals(true, newConfig.get(List.of("features", "featureA", "enabled")));

        // When getting a parent key, it should return the nested map
        assertInstanceOf(Map.class, newConfig.get(List.of("database")));

        @SuppressWarnings("unchecked")
        Map<String, Object> databaseMap = (Map<String, Object>) newConfig.get(List.of("database"));

        assertEquals("localhost", databaseMap.get("host"));
        assertEquals(5432.0, databaseMap.get("port"));
    }

    @Test
    void testGetWithDefault() throws IOException {
        fileConfig.load(); // Load an empty/non-existent file
        assertEquals("default", fileConfig.getOrDefault(List.of("non", "existent", "key"), "default"));
        assertNull(fileConfig.get(List.of("non", "existent", "key")));
        assertEquals("default", fileConfig.getOrDefault(List.of("a", "b", "c"), "default"));

        fileConfig.set(List.of("a", "b", "c"), "value");
        assertEquals("value", fileConfig.getOrDefault(List.of("a", "b", "c"), "default"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetAll() throws IOException {
        fileConfig.set(List.of("a", "x"), 1);
        fileConfig.set(List.of("b", "y"), "two");
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
    void testLoadNonExistentFile() {
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
        fileConfig.set(List.of("a", "b", "c"), "value1");
        assertEquals("value1", fileConfig.get(List.of("a", "b", "c")));

        fileConfig.set(List.of("a", "b", "c"), "value2");
        assertEquals("value2", fileConfig.get(List.of("a", "b", "c")));

        fileConfig.set(List.of("a", "b", "d"), "value3");
        assertEquals("value2", fileConfig.get(List.of("a", "b", "c")));
        assertEquals("value3", fileConfig.get(List.of("a", "b", "d")));

        fileConfig.set(List.of("x"), "value4");
        assertEquals("value4", fileConfig.get(List.of("x")));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testOverwrite() {
        fileConfig.set(List.of("a", "b"), "not a map");
        assertEquals("not a map", fileConfig.get(List.of("a", "b")));

        fileConfig.set(List.of("a", "b", "c"), "value");
        assertEquals("value", fileConfig.get(List.of("a", "b", "c")));

        // After overwriting "a.b" with a map, "a.b" should now be a map
        assertInstanceOf(Map.class, fileConfig.get(List.of("a", "b")));

        Map<String, Object> abMap = (Map<String, Object>) fileConfig.get(List.of("a", "b"));
        assertEquals("value", abMap.get("c"));
    }
}
