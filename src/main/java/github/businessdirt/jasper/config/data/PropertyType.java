package github.businessdirt.jasper.config.data;

import github.businessdirt.jasper.config.render.ConfigRenderer;

/**
 * Defines the type of a configuration property, which influences how it is rendered and its underlying data type.
 * <p>
 * Each type is associated with a specific Java class and is intended to be used with a {@link ConfigRenderer}.
 * The declared type of a field annotated with {@link Property} must match the class associated with the chosen {@code PropertyType}.
 *
 * @see Property
 * @see ConfigRenderer
 */
public enum PropertyType {

    /** A boolean property, typically rendered as a switch or checkbox. */
    SWITCH(Boolean.class),

    /** A string property for multi-line text input, typically rendered as a text area. */
    TEXT(String.class),

    /** A string property for single-line text input, typically rendered as a text field. */
    PARAGRAPH(String.class),

    /** An integer property, typically rendered as a slider for selecting a value within a range. */
    SLIDER(Integer.class),

    /** A numeric property, typically rendered as a text field for number input. */
    NUMBER(Double.class),

    /** A string property where the value is selected from a predefined list of options, typically rendered as a dropdown or radio buttons. */
    SELECTOR(String.class);

    private final Class<?> type;

    PropertyType(Class<?> type) {
        this.type = type;
    }

    /**
     * Gets the underlying Java class associated with this property type.
     * @return the class of the property type.
     */
    public Class<?> getType() {
        return type;
    }
}
