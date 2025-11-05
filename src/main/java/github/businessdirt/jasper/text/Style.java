package github.businessdirt.jasper.text;

public record Style(
        TextColor color,
        boolean bold,
        boolean italic,
        boolean underlined,
        boolean strikethrough
) {

    public static final Style EMPTY = new Style(null, false, false, false, false);

    public Style withColor(TextColor color) {
        return new Style(color, bold, italic, underlined, strikethrough);
    }

    public Style withBold(boolean bold) {
        return new Style(color, bold, italic, underlined, strikethrough);
    }

    public Style withItalic(boolean italic) {
        return new Style(color, bold, italic, underlined, strikethrough);
    }

    public Style withUnderlined(boolean underlined) {
        return new Style(color, bold, italic, underlined, strikethrough);
    }

    public Style withStrikethrough(boolean strikethrough) {
        return new Style(color, bold, italic, underlined, strikethrough);
    }

    public Style merge(Style other) {
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
