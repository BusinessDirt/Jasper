package github.businessdirt.jasper.config.data;

import github.businessdirt.jasper.config.ConfigHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * A record that encapsulates all data related to a configuration property.
 * <p>
 * This class holds the {@link Property} annotation, the {@link Field} it is attached to,
 * and the {@link ConfigHandler} instance that owns the field. It provides convenient methods
 * for getting and setting the property's value.
 *
 * @param options the {@link Property} annotation containing metadata.
 * @param field   the {@link Field} representing the property.
 * @param owner   the {@link ConfigHandler} instance that owns this property.
 */
public record PropertyData(
        @NotNull Property options,
        @NotNull Field field,
        @NotNull ConfigHandler owner
) {

    /**
     * Retrieves the current value of the property's field.
     *
     * @param expectedType the expected class of the return type.
     * @param <T>          the generic type of the value.
     * @return the current value of the property's field, cast to the expected type, or {@code null} if the
     *         value is null, not an instance of the expected type, or if access is denied.
     */
    public <T> @Nullable T getValue(Class<T> expectedType) {
        try {
            Object value = this.field.get(owner);

            if (value == null) return null;
            if (!expectedType.isInstance(value)) return null;

            return expectedType.cast(value);

        } catch (IllegalAccessException _) {
            return null;
        }
    }

    /**
     * Sets the value of the property's field.
     * <p>
     * This method handles type conversion for numeric types, which is particularly useful when deserializing
     * from formats like JSON, where all numbers might be parsed as a single type (e.g., {@code Double}).
     *
     * @param value the new value to be set for the property.
     */
    public void setValue(@NotNull Object value) {
        try {
            Class<?> fieldType = this.field.getType();

            if (value instanceof Number numValue) {
                if (fieldType == int.class || fieldType == Integer.class) {
                    this.field.set(owner, numValue.intValue());
                    return;
                } else if (fieldType == long.class || fieldType == Long.class) {
                    this.field.set(owner, numValue.longValue());
                    return;
                } else if (fieldType == double.class || fieldType == Double.class) {
                    this.field.set(owner, numValue.doubleValue());
                    return;
                } else if (fieldType == float.class || fieldType == Float.class) {
                    this.field.set(owner, numValue.floatValue());
                    return;
                } else if (fieldType == short.class || fieldType == Short.class) {
                    this.field.set(owner, numValue.shortValue());
                    return;
                } else if (fieldType == byte.class || fieldType == Byte.class) {
                    this.field.set(owner, numValue.byteValue());
                    return;
                }
            }

            this.field.set(owner, value);
        } catch (IllegalAccessException _) { }
    }
}
