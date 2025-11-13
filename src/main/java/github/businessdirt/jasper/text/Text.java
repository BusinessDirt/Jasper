package github.businessdirt.jasper.text;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class for all text components.
 * A text component has a {@link Style} and a list of siblings.
 * The siblings are other text components that are appended to this one.
 */
public abstract class Text {

    /**
     * The style of this text component.
     */
    protected Style style;

    /**
     * The list of siblings of this text component.
     */
    protected final List<Text> siblings;

    /**
     * Constructs a new text component with an empty style and an empty list of siblings.
     */
    protected Text() {
        this(Style.EMPTY, new ArrayList<>());
    }

    /**
     * Constructs a new text component with the given style and siblings.
     * @param style the style of the text component
     * @param siblings the list of siblings
     */
    protected Text(Style style, List<Text> siblings) {
        this.style = style;
        this.siblings = siblings;
    }

    /**
     * Appends a text component to this one.
     * @param text the text component to append
     * @return this text component
     */
    public Text append(Text text) {
        siblings.add(text);
        return this;
    }

    /**
     * Sets the style of this text component.
     * @param style the new style
     * @return this text component
     */
    public Text setStyle(Style style) {
        this.style = style;
        return this;
    }

    /**
     * @return the style of this text component
     */
    public Style getStyle() {
        return style;
    }

    /**
     * @return the list of siblings of this text component
     */
    public List<Text> getSiblings() {
        return siblings;
    }

    /**
     * Returns the plain string representation of this text component.
     * This does not include the siblings.
     * @return the plain string representation
     */
    public abstract String asString();

    /**
     * Creates a new {@link LiteralText} component with the given text.
     * @param text the text content
     * @return a new literal text component
     */
    public static Text literal(String text) {
        return new LiteralText(text);
    }
}
