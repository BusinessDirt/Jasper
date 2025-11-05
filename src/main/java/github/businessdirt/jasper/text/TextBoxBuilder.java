/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.utils.text;

import bollschweiler.de.lmu.ifi.cip.gitlab2.utils.text.types.CenteredLiteralText;
import bollschweiler.de.lmu.ifi.cip.gitlab2.utils.text.types.Divider;
import bollschweiler.de.lmu.ifi.cip.gitlab2.utils.text.types.HeaderText;
import bollschweiler.de.lmu.ifi.cip.gitlab2.utils.text.types.LiteralText;
import java.util.ArrayList;
import java.util.List;

/**
 * A builder class for constructing formatted text boxes with various text elements like literals, centered text, headers, and dividers.
 * It automatically calculates the necessary padding and formatting to create a visually appealing box.
 */
public class TextBoxBuilder {

    private final HeaderText header;
    private final List<Text> content;

    /**
     * Constructs a new TextBoxBuilder with a formatted header.
     *
     * @param headerFormat The format string for the header.
     * @param args Arguments to be formatted into the header string.
     */
    public TextBoxBuilder(String headerFormat, Object... args) {
        this.header = new HeaderText(String.format(headerFormat, args));
        this.content = new ArrayList<>();
    }

    /**
     * Constructs a new TextBoxBuilder with a simple header.
     *
     * @param header The header text.
     */
    public TextBoxBuilder(String header) {
        this.header = new HeaderText(header);
        this.content = new ArrayList<>();
    }

    /**
     * Constructs a new TextBoxBuilder without a header.
     */
    public TextBoxBuilder() {
        this.header = null;
        this.content = new ArrayList<>();
    }

    /**
     * Adds a literal text line to the text box.
     *
     * @param line The literal text content.
     * @return The TextBoxBuilder instance for chaining.
     */
    public TextBoxBuilder literal(String line) {
        this.content.add(new LiteralText(line));
        return this;
    }

    /**
     * Adds a formatted literal text line to the text box.
     *
     * @param format The format string for the literal text.
     * @param args Arguments to be formatted into the literal text string.
     * @return The TextBoxBuilder instance for chaining.
     */
    public TextBoxBuilder literal(String format, Object... args) {
        this.content.add(new LiteralText(String.format(format, args)));
        return this;
    }

    /**
     * Adds a centered literal text line to the text box.
     *
     * @param line The centered literal text content.
     * @return The TextBoxBuilder instance for chaining.
     */
    public TextBoxBuilder centeredLiteral(String line) {
        this.content.add(new CenteredLiteralText(line));
        return this;
    }

    /**
     * Adds a formatted centered literal text line to the text box.
     *
     * @param format The format string for the centered literal text.
     * @param args Arguments to be formatted into the centered literal text string.
     * @return The TextBoxBuilder instance for chaining.
     */
    public TextBoxBuilder centeredLiteral(String format, Object... args) {
        this.content.add(new CenteredLiteralText(String.format(format, args)));
        return this;
    }

    /**
     * Adds a header text line to the text box.
     *
     * @param line The header text content.
     * @return The TextBoxBuilder instance for chaining.
     */
    public TextBoxBuilder header(String line) {
        this.content.add(new HeaderText(line));
        return this;
    }

    /**
     * Adds a formatted header text line to the text box.
     *
     * @param format The format string for the header text.
     * @param args Arguments to be formatted into the header text string.
     * @return The TextBoxBuilder instance for chaining.
     */
    public TextBoxBuilder header(String format, Object... args) {
        this.content.add(new HeaderText(String.format(format, args)));
        return this;
    }

    /**
     * Adds a divider line to the text box.
     *
     * @return The TextBoxBuilder instance for chaining.
     */
    public TextBoxBuilder divider() {
        this.content.add(new Divider());
        return this;
    }

    /**
     * Builds the final formatted text box string.
     *
     * @return The complete text box as a string.
     */
    public String build() {
        int longestLineLength = this.getLongestLineLength();
        int headerExtraLength = this.getHeaderExtraLength(longestLineLength);
        if (headerExtraLength % 2 != 0) longestLineLength += 1;

        StringBuilder textBox = new StringBuilder();

        if (this.header != null) {
            textBox.append(header.getCustomPaddedAndFormattedMessage(longestLineLength, '╭', '╮'));
        } else {
            textBox.append("╭").append("─".repeat(longestLineLength + 2)).append("╮\n");
        }

        for (Text line : content) textBox.append(line.getPaddedAndFormattedMessage(longestLineLength));
        textBox.append("╰").append("─".repeat(longestLineLength + 2)).append("╯");

        return textBox.toString();
    }

    private int getLongestLineLength() {
        int longestLineLength = 0;
        if (this.header != null) {
            // '-- header --' we add three chars in front and back
            // -> +6 is min header length
            longestLineLength = header.content().length() + 6;
        }

        for (Text line : content) {
            if (line.content().length() > longestLineLength)
                longestLineLength = line.content().length();
        }

        return longestLineLength;
    }

    /**
     * Makes the header symmetrical
     */
    private int getHeaderExtraLength(int longestLineLength) {
        if (this.header == null) return 0;
        return longestLineLength - this.header.content().length();
    }
}
