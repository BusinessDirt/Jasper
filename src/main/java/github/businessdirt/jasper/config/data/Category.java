package github.businessdirt.jasper.config.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record Category(
        @NotNull String name,
        @NotNull List<PropertyData> items
) {

    @Override
    public @NotNull String toString() {
        List<String> string = new ArrayList<>();
        this.items.forEach(data -> string.add(data.toString()));
        return "Category \"" + this.name + "\"\n" + String.join("\n", string);
    }
}
