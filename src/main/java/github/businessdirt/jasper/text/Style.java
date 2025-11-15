package github.businessdirt.jasper.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the styling of a {@link Text} component.
 * The styling is immutable, and every modification method returns a new owner.
 *
 * @param color the {@link TextColor} of the text
 * @param bold whether the text is bold
 * @param italic whether the text is italic
 * @param underlined whether the text is underlined
 * @param strikethrough whether the text is strikethrough
 */
public record Style(
        @Nullable TextColor color,
        boolean bold,
        boolean italic,
        boolean underlined,
        boolean strikethrough
) {

    /**
     * An empty style with no formatting.
     */
    public static final Style EMPTY = new Style(null, false, false, false, false);

    /**
     * Returns a new style with the given color.
     * @param color the new color
     * @return a new style owner
     */
    public @NotNull Style withColor(@NotNull TextColor color) {
        return new Style(color, bold, italic, underlined, strikethrough);
    }

    /**
     * Returns a new style with the given bold value.
     * @param bold whether the text should be bold
     * @return a new style owner
     */
    public @NotNull Style withBold(boolean bold) {
        return new Style(color, bold, italic, underlined, strikethrough);
    }

    /**
     * Returns a new style with the given italic value.
     * @param italic whether the text should be italic
     * @return a new style owner
     */
    public @NotNull Style withItalic(boolean italic) {
        return new Style(color, bold, italic, underlined, strikethrough);
    }

    /**
     * Returns a new style with the given underlined value.
     * @param underlined whether the text should be underlined
     * @return a new style owner
     */
    public @NotNull Style withUnderlined(boolean underlined) {
        return new Style(color, bold, italic, underlined, strikethrough);
    }

    /**
     * Returns a new style with the given strikethrough value.
     * @param strikethrough whether the text should have a strikethrough
     * @return a new style owner
     */
    public @NotNull Style withStrikethrough(boolean strikethrough) {
        return new Style(color, bold, italic, underlined, strikethrough);
    }

    /**
     * Merges this style with another style.
     * If a options is not set in the other style, the options from this style is used.
     * @param other the style to merge with
     * @return a new style owner with the merged properties
     */
    public @NotNull Style merge(@NotNull Style other) {
        if (this == EMPTY) return other;
        if (other == EMPTY) return this;

        return new Style(
                other.color != null ? other.color : color,
                other.bold || bold,
                other.italic || italic,
                other.underlined || underlined,
                other.strikethrough || strikethrough
        );
    }
}
