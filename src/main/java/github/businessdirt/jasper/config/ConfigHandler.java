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

    public ConfigHandler(@NotNull Path configFilePath) {
        this.configFile = new FileConfig(configFilePath);
        this.properties = new ArrayList<>();

            Arrays.stream(this.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Property.class))
                .forEach(field -> {
                    field.setAccessible(true);

                    Property property = field.getAnnotation(Property.class);
                    PropertyData data = PropertyData.fromField(property, field, this);
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

    public final @NotNull List<Category> getCategories() {
        Map<String, List<PropertyData>> categoryMap = new LinkedHashMap<>();

        this.properties.stream()
                .filter(data -> !data.property().hidden())
                .forEach(propertyData -> {
                    String category = propertyData.property().category();

                    List<PropertyData> dataList = categoryMap.get(category);
                    if (dataList == null) dataList = new ArrayList<>();
                    dataList.add(propertyData);

                    categoryMap.put(category, dataList);
                });

        return categoryMap.entrySet().stream()
                .map(entry -> {
                    entry.getValue().sort(new SubcategoryComparator());
                    return new Category(entry.getKey(), entry.getValue());
                })
                .toList();
    }

    private void readData() throws IOException {
        this.configFile.load();

        for (PropertyData propertyData : this.properties) {
            String propertyPath = ConfigHandler.fullPropertyPath(propertyData.property());
            Object configObject = this.configFile.get(propertyPath);

            if (configObject == null) configObject = propertyData.getValue();
            propertyData.setValue(configObject);
        }
    }

    public final void writeData() throws IOException {
        if (!this.dirty) return;

        for (PropertyData propertyData : this.properties) {
            String propertyPath = ConfigHandler.fullPropertyPath(propertyData.property());
            Object propertyValue = propertyData.getValue();

            this.configFile.set(propertyPath, propertyValue);
        }

        this.configFile.save();
        this.dirty = false;
    }

    private static @NotNull String fullPropertyPath(@NotNull Property fullPropertyPath) {
        StringBuilder bobTheBuilder = new StringBuilder();
        bobTheBuilder.append(fullPropertyPath.category()).append(".");

        if (!Objects.equals(fullPropertyPath.subcategory(), "")) {
            bobTheBuilder.append(fullPropertyPath.subcategory()).append(".");
        }

        bobTheBuilder.append(fullPropertyPath.name());
        return bobTheBuilder.toString();
    }

    public final @NotNull List<PropertyData> getProperties() {
        return this.properties;
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

    private static class SubcategoryComparator implements Comparator<PropertyData> {

        @Override
        public int compare(PropertyData o1, PropertyData o2) {
            return o1.property().subcategory().compareTo(o2.property().subcategory());
        }
    }
}
