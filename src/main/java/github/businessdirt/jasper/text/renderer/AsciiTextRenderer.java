package github.businessdirt.jasper.text.renderer;

import github.businessdirt.jasper.text.Style;
import github.businessdirt.jasper.text.Text;
import github.businessdirt.jasper.text.TextColor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A text renderer that converts a {@link Text} component into a string with ASCII escape codes.
 */
public class AsciiTextRenderer implements TextRenderer<String> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String render(Text text) {
        StringBuilder sb = new StringBuilder();
        this.renderComponent(sb, text);
        return sb.toString();
    }

    /**
     * Renders a text component and its siblings to the given string builder.
     * @param sb the string builder
     * @param text the text component to render
     */
    private void renderComponent(StringBuilder sb, Text text) {
        sb.append(toAnsi(text.getStyle()));
        sb.append(text.asString());
        sb.append("\033[0m");

        for (Text sibling : text.getSiblings())
            this.renderComponent(sb, sibling);
    }

    /**
     * Converts a {@link Style} into an ASCII escape code.
     * @param style the style to convert
     * @return the ASCII escape code
     */
    private String toAnsi(Style style) {
        if (style == Style.EMPTY) return "";

        List<String> codes = new ArrayList<>();
        if (style.bold()) codes.add("1");
        if (style.italic()) codes.add("3");
        if (style.underlined()) codes.add("4");
        if (style.strikethrough()) codes.add("9");

        TextColor textColor = style.color();
        if (textColor != null) {
            Color color = new Color(textColor.rgb());
            codes.add("38");
            codes.add("2");
            codes.add(String.valueOf(color.getRed()));
            codes.add(String.valueOf(color.getGreen()));
            codes.add(String.valueOf(color.getBlue()));
        }

        if (codes.isEmpty()) return "";

        return "[" + String.join(";", codes) + "m";
    }
}
