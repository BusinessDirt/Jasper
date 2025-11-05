package github.businessdirt.jasper.text.renderer;

import github.businessdirt.jasper.text.Style;
import github.businessdirt.jasper.text.Text;
import github.businessdirt.jasper.text.TextColor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsciiRenderer {

    private final Random random = new Random();

    public String render(Text text) {
        StringBuilder sb = new StringBuilder();
        renderComponent(sb, text);
        return sb.toString();
    }

    private void renderComponent(StringBuilder sb, Text text) {
        Style style = text.getStyle();
        String content = text.asString();

        sb.append(toAnsi(style));
        sb.append(content);
        sb.append("[0m");

        for (Text sibling : text.getSiblings()) {
            renderComponent(sb, sibling);
        }
    }

    private String toAnsi(Style style) {
        if (style == Style.EMPTY) {
            return "";
        }

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

        if (codes.isEmpty()) {
            return "";
        }

        return "[" + String.join(";", codes) + "m";
    }
}
