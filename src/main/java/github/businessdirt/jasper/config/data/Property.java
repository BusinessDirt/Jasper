package github.businessdirt.jasper.config.data;

import github.businessdirt.jasper.config.ConfigHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as a configuration property within a class that extends {@link ConfigHandler}.
 * <p>
 * Fields annotated with {@code @Property} are automatically discovered and managed by the {@link ConfigHandler}.
 * This annotation provides metadata for the property, such as its type, name, description, and category,
 * which is used for serialization and can be used for rendering in a user interface.
 *
 * @see ConfigHandler
 * @see PropertyType
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    /**
     * The type of the property. This determines the underlying JVM type and is used by a
     * {@link github.businessdirt.jasper.config.render.ConfigRenderer} to display the appropriate UI control.
     * @return the {@link PropertyType} of the property.
     */
    @NotNull PropertyType type();

    /**
     * The display name of the property. This is used for serialization and can be shown in a user interface.
     * @return the name of the property.
     */
    @NotNull String name();

    /**
     * An optional description of the property. This can be used to provide more context in a user interface.
     * @return the description of the property.
     */
    @NotNull String description() default "";

    /**
     * The category to which this property belongs. Properties can be grouped by category.
     * @return the category of the property.
     */
    @NotNull String category();

    /**
     * An optional subcategory for more granular grouping within a category.
     * @return the subcategory of the property.
     */
    @NotNull String subcategory() default "";

    /**
     * The minimum value for a property of type {@link PropertyType#SLIDER}.
     * @return the minimum allowed value.
     */
    int min() default 0;

    /**
     * The maximum value for a property of type {@link PropertyType#SLIDER}.
     * @return the maximum allowed value.
     */
    int max() default 0;

    /**
     * The selectable choices for a property of type {@link PropertyType#SELECTOR}.
     * @return an array of string options.
     */
    @NotNull String[] options() default {};

    /**
     * Specifies whether this property should be hidden from rendering.
     * @return {@code true} if the property should be hidden, {@code false} otherwise.
     */
    boolean hidden() default false;
}
