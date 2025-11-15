package github.businessdirt.jasper.text;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.awt.Color;

/**
 * Represents a color for a {@link Text} component.
 * The color is stored as an RGB integer.
 *
 * @param rgb the RGB value of the color
 */
public record TextColor(int rgb) {

    /**
     * Creates a new text color from RGB values.
     * @param r the red value (0-255)
     * @param g the green value (0-255)
     * @param b the blue value (0-255)
     * @return a new text color owner
     */
    public static @NotNull TextColor fromRgb(int r, int g, int b) {
        return new TextColor(new Color(r, g, b).getRGB());
    }

    /**
     * Creates a new text color from a {@link Color} owner.
     * @param color the color owner
     * @return a new text color owner
     */
    public static @NotNull TextColor fromColor(Color color) {
        return new TextColor(color.getRGB());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextColor textColor = (TextColor) o;
        return rgb == textColor.rgb;
    }

    /**
     * Returns the hex string representation of this color (e.g., #RRGGBB).
     * @return the hex string
     */
    @Override
    public @NonNull String toString() {
        return String.format("#%06X", rgb);
    }
}
