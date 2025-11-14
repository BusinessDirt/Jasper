package github.businessdirt.jasper.text;

import org.jetbrains.annotations.NotNull;

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
    protected Text(
            @NotNull Style style,
            @NotNull List<Text> siblings
    ) {
        this.style = style;
        this.siblings = siblings;
    }

    /**
     * Appends a text component to this one.
     * @param text the text component to append
     * @return this text component
     */
    public @NotNull Text append(@NotNull Text text) {
        siblings.add(text);
        return this;
    }

    /**
     * Sets the style of this text component.
     * @param style the new style
     * @return this text component
     */
    public @NotNull Text setStyle(@NotNull Style style) {
        this.style = style;
        return this;
    }

    /**
     * @return the style of this text component
     */
    public @NotNull Style getStyle() {
        return this.style;
    }

    /**
     * @return the list of siblings of this text component
     */
    public @NotNull List<Text> getSiblings() {
        return this.siblings;
    }

    /**
     * Returns the plain string representation of this text component.
     * This does not include the siblings.
     * @return the plain string representation
     */
    public abstract @NotNull String asString();

    /**
     * Creates a new {@link LiteralText} component with the given text.
     * @param text the text content
     * @return a new literal text component
     */
    public static @NotNull Text literal(@NotNull String text) {
        return new LiteralText(text);
    }
}
