/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.text.types;

import github.businessdirt.jasper.text.Text;

/**
 * Represents a literal text element that is centered within a text box.
 */
public class CenteredLiteralText extends Text {

    /**
     * Constructs a new CenteredLiteralText with the given content.
     *
     * @param content The string content to be centered.
     */
    public CenteredLiteralText(String content) {
        super(content);
    }

    /**
     * Returns the centered and padded message for display within a text box.
     * The content is padded with spaces on both sides to achieve centering.
     *
     * @param linePadWidth The desired width for padding the line.
     * @return The formatted string ready for display.
     */
    @Override
    public String getPaddedAndFormattedMessage(int linePadWidth) {
        int whitespaces = linePadWidth - this.content.length();
        int whitespacesLeft = Math.floorDiv(whitespaces, 2);
        int whiteSpacesRight = whitespaces - whitespacesLeft;

        return "│ " + " ".repeat(whitespacesLeft) + this.content + " ".repeat(whiteSpacesRight) + " │\n";
    }
}
