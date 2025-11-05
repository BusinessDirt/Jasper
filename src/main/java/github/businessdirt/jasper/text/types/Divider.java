/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.text.types;

import github.businessdirt.jasper.text.Text;

/**
 * Represents a horizontal divider line used within a text box.
 */
public class Divider extends Text {

    /**
     * Constructs a new Divider. The content is a single dash, which will be repeated to form the line.
     */
    public Divider() {
        super("─");
    }

    /**
     * Returns the formatted divider line for display within a text box.
     * The dash character is repeated to fill the specified line width.
     *
     * @param linePadWidth The desired width for the divider line.
     * @return The formatted divider string.
     */
    @Override
    public String getPaddedAndFormattedMessage(int linePadWidth) {
        return "├" + this.content.repeat(linePadWidth + 2) + "┤\n";
    }
}
