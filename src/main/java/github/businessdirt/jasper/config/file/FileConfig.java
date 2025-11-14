package github.businessdirt.jasper.config.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileConfig {

    private final Path configPath;
    private final Gson gson;
    private final Map<String, Object> store;

    /**
     * Creates a new FileConfig instance.
     *
     * @param configPath The path to the JSON file (e.g., Paths.get("config.json"))
     */
    public FileConfig(@NotNull Path configPath) {
        this.configPath = configPath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.store = new HashMap<>();
    }

    /**
     * Loads the configuration from disk.
     * Starts with defaults, then overlays the file's content.
     */
    public void load() throws IOException, JsonSyntaxException {
        // Start with the provided defaults
        this.store.clear();
        if (!Files.exists(configPath)) return;

        // Define the type for Gson. This is crucial for deserializing
        // a generic Map<String, Object>.
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

        try (FileReader reader = new FileReader(configPath.toFile())) {
            Map<String, Object> fromFile = gson.fromJson(reader, mapType);
            if (fromFile != null) this.store.putAll(fromFile);
        }
    }

    /**
     * Saves the entire current configuration map to the JSON file.
     */
    public void save() throws IOException {
        // Ensure parent directory exists
        Path parentDir = configPath.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        // Write the 'store' map to the file
        try (FileWriter writer = new FileWriter(configPath.toFile())) {
            gson.toJson(this.store, writer);
        }
    }

    /**
     * Sets a value for a given key.
     * The key is split by dots to create a nested map structure.
     *
     * @param path   The configuration path (e.g., ["window", "size", "width"])
     * @param value The value to store. Can be a string, number, boolean, map, list, etc.
     */
    public void set(@NotNull List<String> path, @NotNull Object value) {
        Map<String, Object> currentMap = this.store;

        for (int i = 0; i < path.size() - 1; i++) {
            String part = path.get(i);
            Object mapValue = currentMap.get(part);

            if (mapValue instanceof Map) {
                //noinspection unchecked
                currentMap = (Map<String, Object>) mapValue;
            } else {
                Map<String, Object> newMap = new HashMap<>();
                currentMap.put(part, newMap);
                currentMap = newMap;
            }
        }

        currentMap.put(path.getLast(), value);
    }

    /**
     * Gets a raw object value from the store.
     * The key is split by dots to traverse a nested map structure.
     *
     * @param path The configuration path.
     * @return The value as an Object, or null if not found.
     */
    public @Nullable Object get(@NotNull List<String> path) {
        Map<String, Object> currentMap = this.store;

        for (int i = 0; i < path.size() - 1; i++) {
            String part = path.get(i);
            Object mapValue = currentMap.get(part);

            if (mapValue instanceof Map) {
                //noinspection unchecked
                currentMap = (Map<String, Object>) mapValue;
            } else {
                return null;
            }
        }

        return currentMap.get(path.getLast());
    }

    /**
     * Gets a value, providing a default if the key is not found.
     * The key is split by dots to traverse a nested map structure.
     *
     * @param path          The path.
     * @param defaultValue The default value.
     * @return The found value or the default.
     */
    @SuppressWarnings("unchecked")
    public <T> @NotNull T getOrDefault(
            @NotNull List<String> path,
            @NotNull T defaultValue
    ) {
        Object value = get(path);
        return value != null ? (T) value : defaultValue;
    }

    /**
     * Returns an immutable copy of the entire configuration map.
     */
    public @NotNull Map<String, Object> getAll() {
        return Collections.unmodifiableMap(this.store);
    }
}
