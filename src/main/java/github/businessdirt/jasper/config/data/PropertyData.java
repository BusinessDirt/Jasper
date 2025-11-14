package github.businessdirt.jasper.config.data;

import github.businessdirt.jasper.config.ConfigHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public record PropertyData(
        @NotNull Property property,
        @NotNull Field field,
        @NotNull ConfigHandler instance
) {

    public <T> @Nullable T getValue() {
        try {
            //noinspection unchecked
            return (T) this.field.get(instance);
        } catch (IllegalAccessException _) { }

        return null;
    }

    public void setValue(@NotNull Object value) {
        try {
            Class<?> fieldType = this.field.getType();

            if (value instanceof Number numValue) {
                if (fieldType == int.class || fieldType == Integer.class) {
                    this.field.set(instance, numValue.intValue());
                    return;
                } else if (fieldType == long.class || fieldType == Long.class) {
                    this.field.set(instance, numValue.longValue());
                    return;
                } else if (fieldType == double.class || fieldType == Double.class) {
                    this.field.set(instance, numValue.doubleValue());
                    return;
                } else if (fieldType == float.class || fieldType == Float.class) {
                    this.field.set(instance, numValue.floatValue());
                    return;
                } else if (fieldType == short.class || fieldType == Short.class) {
                    this.field.set(instance, numValue.shortValue());
                    return;
                } else if (fieldType == byte.class || fieldType == Byte.class) {
                    this.field.set(instance, numValue.byteValue());
                    return;
                }
            }

            // For all other types (Boolean->boolean, String->String, etc.)
            // the default set() will work.
            this.field.set(instance, value);
        } catch (IllegalAccessException _) { }
    }
}
