/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands;

/** A simple string reader that allows reading a string character by character. */
public class StringReader {
    private final String string;
    private int cursor;

    /**
     * Constructs a new string reader.
     *
     * @param string the string to read
     */
    public StringReader(String string) {
        this.string = string;
    }

    /**
     * Constructs a new string reader from another string reader.
     *
     * @param other the other string reader
     */
    public StringReader(StringReader other) {
        this.string = other.string;
        this.cursor = other.cursor;
    }

    /**
     * Reads a string until a whitespace is found.
     *
     * @return the read string
     */
    public String readString() {
        int start = cursor;
        while (canRead() && !Character.isWhitespace(peek())) skip();
        return string.substring(start, cursor);
    }

    /**
     * Reads the remaining string.
     *
     * @return the remaining string
     */
    public String readRemaining() {
        String remaining = string.substring(cursor);
        cursor = string.length();
        return remaining;
    }

    /**
     * Checks if the reader can read.
     *
     * @return true if the reader can read, false otherwise
     */
    public boolean canRead() {
        return canRead(1);
    }

    /**
     * Checks if the reader can read a certain length.
     *
     * @param length the length to check
     * @return true if the reader can read the given length, false otherwise
     */
    public boolean canRead(int length) {
        return cursor + length <= string.length();
    }

    /**
     * Peeks the next character without consuming it.
     *
     * @return the next character
     */
    public char peek() {
        return string.charAt(cursor);
    }

    /** Skips the next character. */
    public void skip() {
        cursor++;
    }

    /**
     * Returns the current cursor position.
     *
     * @return the current cursor position
     */
    public int getCursor() {
        return cursor;
    }

    /**
     * Sets the cursor position.
     *
     * @param cursor the new cursor position
     */
    public void setCursor(int cursor) {
        this.cursor = cursor;
    }
}
