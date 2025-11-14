package github.businessdirt.jasper.config.data;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    @NotNull PropertyType type();

    @NotNull String name();
    @NotNull String description() default "";

    @NotNull String category();
    @NotNull String subcategory() default "";

    // Range of numbers for Sliders
    int min() default 0;
    int max() default 0;

    // Options for Selectors
    String[] options() default {};

    boolean hidden() default false;
}
