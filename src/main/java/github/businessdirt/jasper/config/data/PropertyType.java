package github.businessdirt.jasper.config.data;

/**
 * Defines the Type of Actor that will be displayed in the Settings Screen <br>
 * The following {@link PropertyType}s can be paired with the following Data Types <br>
 *      {@code SWITCH} -> {@link Boolean} <br>
 *      {@code TEXT} -> {@link String} <br>
 *      {@code PARAGRAPH} -> {@link String} <br>
 *      {@code SLIDER} -> {@link Integer} <br>
 *      {@code NUMBER} -> {@link Double} <br>
 *      {@code SELECTOR} -> {@link String} <br>
 */
public enum PropertyType {
    SWITCH, TEXT, PARAGRAPH, SLIDER, NUMBER, SELECTOR
}
