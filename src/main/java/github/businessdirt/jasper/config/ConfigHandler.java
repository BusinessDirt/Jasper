package github.businessdirt.jasper.config;

import github.businessdirt.jasper.config.data.Property;
import github.businessdirt.jasper.config.data.PropertyData;
import github.businessdirt.jasper.config.file.FileConfig;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * An abstract configuration handler that manages loading, saving, and accessing configuration properties.
 * <p>
 * This class uses reflection to find all fields annotated with {@link Property} and manages their values.
 * It automatically handles reading from and writing to a JSON configuration file.
 * It also provides a mechanism to periodically save changes and ensures data is saved on application shutdown.
 *
 * @see Property
 * @see FileConfig
 */
@SuppressWarnings("unused")
public abstract class ConfigHandler {

    private final List<PropertyData> properties;
    private final FileConfig configFile;
    private boolean dirty;

    /**
     * Constructs a new {@code ConfigHandler}.
     * <p>
     * It initializes the configuration file and uses reflection to discover all fields
     * annotated with {@link Property}. These fields are then registered as configuration properties.
     *
     * @param configFilePath the path to the configuration file.
     */
    @SuppressWarnings("this-escape")
    public ConfigHandler(
            @NotNull Path configFilePath
    ) {
        this.configFile = new FileConfig(configFilePath);
        this.properties = new ArrayList<>();

        Arrays.stream(this.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Property.class))
            .forEach(field -> {
                field.setAccessible(true);

                Property property = field.getAnnotation(Property.class);
                PropertyData data = new PropertyData(property, field, this);
                this.properties.add(data);
            });
    }

    /**
     * Initializes the configuration handler.
     * <p>
     * This method performs the initial data read from the configuration file. It then sets up a periodic task
     * to save any changes every 30 seconds and registers a shutdown hook to ensure any pending changes are
     * written to disk when the application exits.
     *
     * @throws IOException if an I/O error occurs during the initial data read.
     */
    public final void initialize() throws IOException {
        this.readData();

        new AutoSaveTimer(this, 60000L);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ConfigHandler.this.writeData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    /**
     * Marks the configuration as dirty.
     * <p>
     * This indicates that one or more properties have been modified and need to be saved to the file.
     * The configuration will be written to disk during the next scheduled save or on application shutdown.
     */
    public final void markDirty() {
        this.dirty = true;
    }

    /**
     * Reads configuration data from the file and updates the properties.
     * <p>
     * For each registered property, it attempts to read the value from the configuration file.
     * If a value is not present in the file, the property retains its default value.
     *
     * @throws IOException if an I/O error occurs while loading the configuration file.
     */
    private void readData() throws IOException {
        this.configFile.load();

        this.properties.forEach(data -> {
            List<String> propertyPath = ConfigHandler.generatePropertyPath(data.options());
            Object configObject = this.configFile.get(propertyPath);

            if (configObject == null) configObject = data.getValue(Object.class);

            // should not be null here anymore
            assert configObject != null;
            data.setValue(configObject);
        });
    }

    /**
     * Writes the current state of the configuration properties to the file.
     * <p>
     * This method only performs a write operation if the configuration has been marked as dirty.
     * After a successful write, the dirty flag is reset.
     *
     * @throws IOException if an I/O error occurs while saving the configuration file.
     */
    public final void writeData() throws IOException {
        if (!this.dirty) return;

        this.properties.forEach(data -> {
            List<String> propertyPath = ConfigHandler.generatePropertyPath(data.options());
            Object propertyValue = data.getValue(Object.class);

            // should not be null here
            assert propertyValue != null;
            this.configFile.set(propertyPath, propertyValue);
        });

        this.configFile.save();
        this.dirty = false;
    }

    /**
     * Generates a hierarchical path for a property based on its category, subcategory, and name.
     *
     * @param property the {@link Property} annotation containing the path components.
     * @return a list of strings representing the nested path of the property.
     */
    private static @NotNull List<String> generatePropertyPath(
            @NotNull Property property
    ) {
        List<String> path = new ArrayList<>(Collections.singleton(property.category()));

        if (!Objects.equals(property.subcategory(), "")) path.add(property.subcategory());

        path.add(property.name());
        return path;
    }

    /**
     * Retrieves the list of all registered properties.
     *
     * @return a list of {@link PropertyData} objects.
     */
    public final @NotNull List<PropertyData> getProperties() {
        return this.properties;
    }

    private static final class AutoSaveTimer extends Timer {

        private final ConfigHandler configHandlerInstance;

        @SuppressWarnings("this-escape")
        public AutoSaveTimer(ConfigHandler configHandlerInstance, long period) {
            super(configHandlerInstance.getClass().getSimpleName() + "::autoSave()", false);
            this.configHandlerInstance = configHandlerInstance;

            this.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    try {
                        AutoSaveTimer.this.configHandlerInstance.writeData();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, 0L, period);
        }
    }
}
