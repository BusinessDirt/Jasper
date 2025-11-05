/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.text;

import java.util.Objects;

/**
 * Abstract base class for different types of text elements used in the text box builder.
 * Provides common functionality for text content and defines an abstract method for formatting.
 */
public abstract class Text {
    protected final String content;

    /**
     * Constructs a new Text object with the given content.
     *
     * @param content The string content of the text element.
     */
    public Text(String content) {
        this.content = content;
    }

    /**
     * Returns the padded and formatted message for display within a text box.
     * The implementation of this method will vary based on the specific text type (e.g., literal, centered, header).
     *
     * @param linePadWidth The desired width for padding the line.
     * @return The formatted string ready for display.
     */
    public abstract String getPaddedAndFormattedMessage(int linePadWidth);

    /**
     * Returns the raw content of the text element.
     *
     * @return The string content.
     */
    public String content() {
        return content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Text) obj;
        return Objects.equals(this.content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return "Text[" +
                "content=" + content + ']';
    }

}
