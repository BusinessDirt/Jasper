package github.businessdirt.jasper.config;

import github.businessdirt.jasper.config.data.Category;
import github.businessdirt.jasper.config.data.Property;
import github.businessdirt.jasper.config.data.PropertyData;
import github.businessdirt.jasper.config.file.FileConfig;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("unused")
public class ConfigHandler {

    private final List<PropertyData> properties;
    private final FileConfig configFile;
    private boolean dirty;

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

    public final void initialize() throws IOException {
        this.readData();

        new Timer("", false)
                .scheduleAtFixedRate(new InitializationTimerTask(this), 0L, 30000L);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ConfigHandler.this.writeData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public final void markDirty() {
        this.dirty = true;
    }

    private void readData() throws IOException {
        this.configFile.load();

        this.properties.forEach(data -> {
            List<String> propertyPath = ConfigHandler.generatePropertyPath(data.property());
            Object configObject = this.configFile.get(propertyPath);

            if (configObject == null) configObject = data.getValue(Object.class);

            // should not be null here anymore
            assert configObject != null;
            data.setValue(configObject);
        });
    }

    public final void writeData() throws IOException {
        if (!this.dirty) return;

        this.properties.forEach(data -> {
            List<String> propertyPath = ConfigHandler.generatePropertyPath(data.property());
            Object propertyValue = data.getValue(Object.class);

            // should not be null here
            assert propertyValue != null;
            this.configFile.set(propertyPath, propertyValue);
        });

        this.configFile.save();
        this.dirty = false;
    }

    private static @NotNull List<String> generatePropertyPath(
            @NotNull Property property
    ) {
        List<String> path = new ArrayList<>(Collections.singleton(property.category()));

        if (!Objects.equals(property.subcategory(), "")) path.add(property.subcategory());

        path.add(property.name());
        return path;
    }

    public final @NotNull List<PropertyData> getProperties() {
        return this.properties;
    }

    public final @NotNull List<Category> getCategories() {
        Map<String, List<PropertyData>> categoryMap = new LinkedHashMap<>();
        this.properties.stream().filter(data -> !data.property().hidden())
                .forEach(propertyData ->
                        Objects.requireNonNull(categoryMap.putIfAbsent(
                                        propertyData.property().category(),
                                        new ArrayList<>()))
                                .add(propertyData));

        return categoryMap.entrySet().stream().map(Category::of).toList();
    }

    private static class InitializationTimerTask extends TimerTask {

        private final ConfigHandler instance;

        public InitializationTimerTask(ConfigHandler instance) {
            this.instance = instance;
        }

        @Override
        public void run() {
            try {
                this.instance.writeData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
