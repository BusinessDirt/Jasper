package github.businessdirt.jasper.config.render;

import github.businessdirt.jasper.config.data.PropertyData;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A record representing a category of configuration properties, used for rendering purposes.
 * <p>
 * This class groups properties under a common category name. It provides a factory method {@link #of(Map.Entry, String)}
 * to create a category from a map entry, which also sorts the properties by subcategory and filters them based on a
 * search query. This is useful for organizing properties in a user interface.
 *
 * @param name  the name of the category.
 * @param items the list of {@link PropertyData} belonging to this category.
 */
public record Category(
        @NotNull String name,
        @NotNull List<PropertyData> items
) {

    /**
     * Creates a {@code Category} from a map entry and filters its properties based on a search query.
     * <p>
     * The properties are sorted by subcategory before being filtered. The filter matches the search query
     * against the property's name, description, category, and subcategory.
     *
     * @param entry       a map entry where the key is the category name and the value is a list of {@link PropertyData}.
     * @param searchQuery the search query to filter the properties. If blank, all properties are included.
     * @return a new {@code Category} object containing the sorted and filtered properties.
     */
    public static Category of(Map.Entry<String, List<PropertyData>> entry, String searchQuery) {
        return new Category(entry.getKey(),
                entry.getValue().stream()
                        .sorted(Comparator.comparing(o -> o.options().subcategory()))
                        .filter(data -> {
                            if (searchQuery.isBlank()) return true;
                            return data.options().name().contains(searchQuery)
                                    || data.options().description().contains(searchQuery)
                                    || data.options().category().contains(searchQuery)
                                    || data.options().subcategory().contains(searchQuery);
                        }).toList()
        );
    }
}
