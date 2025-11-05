package github.businessdirt.jasper.text;

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
    public String asString() {
        return text;
    }
}
