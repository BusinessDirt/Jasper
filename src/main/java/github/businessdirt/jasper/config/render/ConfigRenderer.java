package github.businessdirt.jasper.config.render;

import github.businessdirt.jasper.config.ConfigHandler;
import github.businessdirt.jasper.config.data.PropertyData;

import java.util.*;

/**
 * An abstract class for rendering a configuration managed by a {@link ConfigHandler}.
 * <p>
 * Implementations of this class can render the configuration in various formats (e.g., a GUI, a web page, or a text file).
 * It provides a helper method, {@link #filter(ConfigHandler, String)}, to simplify the process of grouping and filtering
 * properties before rendering.
 *
 * @param <C> the type of the {@link ConfigHandler} this renderer is designed for.
 * @param <T> the type of the object returned by the render method (e.g., a UI component, a string).
 */
public abstract class ConfigRenderer<C extends ConfigHandler, T> {

    /**
     * Renders the configuration.
     *
     * @param config      the configuration handler instance to render.
     * @param searchQuery a query string to filter the properties. Can be empty.
     * @return the rendered output of type {@code T}.
     */
    public abstract T render(C config, String searchQuery);

    /**
     * Filters and groups the configuration properties into categories.
     * <p>
     * This method organizes properties from the {@link ConfigHandler} into a map where keys are category names.
     * It excludes any properties marked as hidden. The resulting map is then converted into a list of {@link Category}
     * objects, which can be easily used for rendering.
     *
     * @param config      the configuration handler containing the properties.
     * @param searchQuery the search query to apply to the properties within each category.
     * @return a list of {@link Category} objects, each containing filtered and structured properties.
     */
    protected List<Category> filter(C config, String searchQuery) {
        Map<String, List<PropertyData>> categoryMap = new LinkedHashMap<>();

        config.getProperties().stream().filter(data -> !data.options().hidden())
                .forEach(propertyData ->
                        categoryMap.computeIfAbsent(propertyData.options().category(), _ ->
                                new ArrayList<>()).add(propertyData));

        return categoryMap.entrySet().stream()
                .map(entry -> Category.of(entry, searchQuery))
                .toList();
    }
}
