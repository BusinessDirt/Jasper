package github.businessdirt.jasper.config.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record Category(
        @NotNull String name,
        @NotNull List<PropertyData> items
) {

    public static Category of(Map.Entry<String, List<PropertyData>> entry) {
        return new Category(entry.getKey(), entry.getValue().stream().sorted(new SubcategoryComparator()).toList());
    }

    @Override
    public @NotNull String toString() {
        List<String> string = new ArrayList<>();
        this.items.forEach(data -> string.add(data.toString()));
        return "Category \"" + this.name + "\"\n" + String.join("\n", string);
    }
}
