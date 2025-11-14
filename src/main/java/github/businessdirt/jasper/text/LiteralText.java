package github.businessdirt.jasper.text;

import org.jetbrains.annotations.NotNull;

/**
 * A simple text component that contains a string.
 */
public class LiteralText extends Text {

    private final String text;

    /**
     * Creates a new literal text component.
     * @param text the text content
     */
    public LiteralText(String text) {
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String asString() {
        return text;
    }
}
