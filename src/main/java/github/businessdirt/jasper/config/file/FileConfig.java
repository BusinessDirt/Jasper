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

/**
 * A utility class for managing a configuration file in JSON format.
 * <p>
 * This class handles the reading and writing of a configuration file using the {@link Gson} library.
 * It stores configuration data in a nested map structure, allowing for hierarchical properties.
 */
public class FileConfig {

    private final Path configPath;
    private final Gson gson;
    private final Map<String, Object> store;

    /**
     * Creates a new {@code FileConfig} instance.
     *
     * @param configPath the path to the JSON configuration file (e.g., {@code Paths.get("config.json")}).
     */
    public FileConfig(@NotNull Path configPath) {
        this.configPath = configPath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.store = new HashMap<>();
    }

    /**
     * Loads the configuration from the JSON file on disk.
     * <p>
     * If the file does not exist, the configuration store remains empty. If the file exists, its contents
     * are parsed and loaded into the in-memory store.
     *
     * @throws IOException         if an I/O error occurs while reading the file.
     * @throws JsonSyntaxException if the file contains invalid JSON.
     */
    public void load() throws IOException, JsonSyntaxException {
        this.store.clear();
        if (!Files.exists(configPath)) return;

        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

        try (FileReader reader = new FileReader(configPath.toFile())) {
            Map<String, Object> fromFile = gson.fromJson(reader, mapType);
            if (fromFile != null) this.store.putAll(fromFile);
        }
    }

    /**
     * Saves the current configuration to the JSON file on disk.
     * <p>
     * This method ensures that the parent directory exists before writing the file.
     *
     * @throws IOException if an I/O error occurs while writing the file.
     */
    public void save() throws IOException {
        Path parentDir = configPath.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        try (FileWriter writer = new FileWriter(configPath.toFile())) {
            gson.toJson(this.store, writer);
        }
    }

    /**
     * Sets a value for a given hierarchical path.
     * <p>
     * The path is used to create or traverse a nested map structure. For example, a path of
     * {@code ["window", "size", "width"]} will result in a structure like {@code { "window": { "size": { "width": value } } } }.
     *
     * @param path  the list of strings representing the configuration path.
     * @param value the value to store (e.g., string, number, boolean, map, list).
     */
    @SuppressWarnings("unchecked")
    public void set(@NotNull List<String> path, @NotNull Object value) {
        Map<String, Object> currentMap = this.store;

        for (int i = 0; i < path.size() - 1; i++) {
            String part = path.get(i);
            Object mapValue = currentMap.get(part);

            if (mapValue instanceof Map) {
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
     * Retrieves a raw object value from a given hierarchical path.
     *
     * @param path the list of strings representing the configuration path.
     * @return the value as an {@link Object}, or {@code null} if the path does not exist.
     */
    @SuppressWarnings("unchecked")
    public @Nullable Object get(@NotNull List<String> path) {
        Map<String, Object> currentMap = this.store;

        for (int i = 0; i < path.size() - 1; i++) {
            String part = path.get(i);
            Object mapValue = currentMap.get(part);

            if (mapValue instanceof Map) {
                currentMap = (Map<String, Object>) mapValue;
            } else {
                return null;
            }
        }

        return currentMap.get(path.getLast());
    }

    /**
     * Retrieves a value from a given path, returning a default value if the path does not exist.
     *
     * @param path         the list of strings representing the configuration path.
     * @param defaultValue the default value to return if the path is not found.
     * @param <T>          the type of the value.
     * @return the found value cast to the specified type, or the default value.
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
     * Returns an immutable view of the entire configuration map.
     *
     * @return an unmodifiable map of the configuration store.
     */
    public @NotNull Map<String, Object> getAll() {
        return Collections.unmodifiableMap(this.store);
    }
}
