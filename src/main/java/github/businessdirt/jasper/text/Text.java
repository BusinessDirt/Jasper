package github.businessdirt.jasper.text;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class Text {

    protected Style style;
    protected final List<Text> siblings;

    protected Text() {
        this(Style.EMPTY, new ArrayList<>());
    }

    protected Text(Style style, List<Text> siblings) {
        this.style = style;
        this.siblings = siblings;
    }

    public Text append(Text text) {
        siblings.add(text);
        return this;
    }

    public Style getStyle() {
        return style;
    }

    public List<Text> getSiblings() {
        return siblings;
    }

    public abstract String asString();

    public static Text literal(String text) {
        return new LiteralText(text);
    }
}
