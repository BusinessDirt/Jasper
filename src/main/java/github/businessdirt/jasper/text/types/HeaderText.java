/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.utils.text.types;

import bollschweiler.de.lmu.ifi.cip.gitlab2.utils.text.Text;

/**
 * Represents a header text element used within a text box.
 * It can be formatted with custom start and end characters.
 */
public class HeaderText extends Text {

    /**
     * Constructs a new HeaderText with the given content.
     *
     * @param content The string content of the header.
     */
    public HeaderText(String content) {
        super(content);
    }

    /**
     * Returns the padded and formatted header message with custom start and end characters.
     * The content is centered and surrounded by padding characters.
     *
     * @param linePadWidth The desired width for padding the line.
     * @param startChar The character to use at the beginning of the header line.
     * @param endChar The character to use at the end of the header line.
     * @return The formatted header string ready for display.
     */
    public String getCustomPaddedAndFormattedMessage(int linePadWidth, char startChar, char endChar) {
        int whitespaces = linePadWidth - this.content.length();
        int whitespacesLeft = Math.floorDiv(whitespaces, 2);
        int whiteSpacesRight = whitespaces - whitespacesLeft;

        return startChar + "─".repeat(whitespacesLeft) + " " + this.content + " " + "─".repeat(whiteSpacesRight) + endChar + "\n";
    }

    /**
     * Returns the padded and formatted header message with default start ('├') and end ('┤') characters.
     *
     * @param linePadWidth The desired width for padding the line.
     * @return The formatted header string ready for display.
     */
    @Override
    public String getPaddedAndFormattedMessage(int linePadWidth) {
        return getCustomPaddedAndFormattedMessage(linePadWidth, '├', '┤');
    }
}
