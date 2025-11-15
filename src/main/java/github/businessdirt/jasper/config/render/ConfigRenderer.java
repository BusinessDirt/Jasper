package github.businessdirt.jasper.config.render;

import github.businessdirt.jasper.config.ConfigHandler;
import github.businessdirt.jasper.config.data.Category;
import github.businessdirt.jasper.config.data.PropertyData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ConfigRenderer<C extends ConfigHandler, T> {

    protected final ConfigHandler config;

    public ConfigRenderer(C config) {
        this.config = config;
    }

    public abstract T render();

    public Map<Category, List<PropertyData>> filter(String searchQuery) {
        HashMap<Category, List<PropertyData>> result = new HashMap<>();

        this.config.getCategories().forEach(category ->
                result.put(category, category.items().stream()
                        .filter(data -> {
                            if (searchQuery.isBlank()) return true;
                            return data.property().name().contains(searchQuery)
                                    || data.property().description().contains(searchQuery)
                                    || data.property().category().contains(searchQuery)
                                    || data.property().subcategory().contains(searchQuery);
                        })
                        .toList()));

        return result;
    }
}
