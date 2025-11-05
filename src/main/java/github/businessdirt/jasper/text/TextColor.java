package github.businessdirt.jasper.text;

import org.jspecify.annotations.NonNull;
import java.awt.Color;

public record TextColor(int rgb) {

    public static TextColor fromRgb(int r, int g, int b) {
        return new TextColor(new Color(r, g, b).getRGB());
    }

    public static TextColor fromColor(Color color) {
        return new TextColor(color.getRGB());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextColor textColor = (TextColor) o;
        return rgb == textColor.rgb;
    }

    @Override
    public @NonNull String toString() {
        return String.format("#%06X", rgb);
    }
}
