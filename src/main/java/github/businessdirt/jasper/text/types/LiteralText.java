/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.utils.text.types;

import bollschweiler.de.lmu.ifi.cip.gitlab2.utils.text.Text;

/**
 * Represents a simple literal text element that is left-aligned within a text box.
 */
public class LiteralText extends Text {

    /**
     * Constructs a new LiteralText with the given content.
     *
     * @param content The string content of the literal text.
     */
    public LiteralText(String content) {
        super(content);
    }

    /**
     * Returns the left-aligned and padded message for display within a text box.
     * The content is padded with spaces on the right to fill the line width.
     *
     * @param linePadWidth The desired width for padding the line.
     * @return The formatted string ready for display.
     */
    @Override
    public String getPaddedAndFormattedMessage(int linePadWidth) {
        int whitespaces = linePadWidth - this.content.length();
        return "│ " + this.content + " ".repeat(whitespaces) + " │\n";
    }
}
