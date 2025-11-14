package github.businessdirt.jasper.config.data;

import github.businessdirt.jasper.config.ConfigHandler;

import java.lang.reflect.Field;

public record PropertyData(
        Property property,
        Field field,
        ConfigHandler instance
) {

    public <T> T getValue() {
        try {
            return (T) this.field.get(instance);
        } catch (IllegalAccessException _) { }
        return null;
    }

    public void setValue(Object value) {
        try {
            this.field.set(instance, value);
        } catch (IllegalAccessException _) { }
    }

    public static PropertyData fromField(Property property, Field field, ConfigHandler instance) {
        return new PropertyData(property, field, instance);
    }
}
