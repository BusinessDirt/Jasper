package github.businessdirt.jasper.streams.charsets;

import java.util.Arrays;

/**
 * Represents a set of 16 box-drawing characters, indexed by a bitmask.
 * This allows for easy retrieval of the correct character based on its
 * required connections (top, bottom, left, right).
 * <p>
 * The bitmask index is calculated as follows:<br>
 * - TOP:    1 (0001)<br>
 * - BOTTOM: 2 (0010)<br>
 * - LEFT:   4 (0100)<br>
 * - RIGHT:  8 (1000)
 * <p>
 * For example, a vertical line '│' connects TOP and BOTTOM,
 * so its index is TOP | BOTTOM = 1 | 2 = 3.
 */
@SuppressWarnings("unused")
public class BoxCharset {

    // --- Internal Bitmask Constants ---
    private static final int TOP    = 1;
    private static final int BOTTOM = 2;
    private static final int LEFT   = 4;
    private static final int RIGHT  = 8;

    /**
     * The 16-character lookup table. The index is the bitmask.
     */
    private final char[] chars;

    /**
     * Creates a new BoxCharset using the 11 primary characters.
     * The remaining 5 characters (empty and 4 end-caps) are defaulted.
     *
     * @param horizontal  Horizontal line (e.g., '─')
     * @param vertical  Vertical line (e.g., '│')
     * @param topLeft Top-left corner (e.g., '┌')
     * @param topRight Top-right corner (e.g., '┐')
     * @param bottomLeft Bottom-left corner (e.g., '└')
     * @param bottomRight Bottom-right corner (e.g., '┘')
     * @param topT Tee-top (e.g., '┬')
     * @param bottomT Tee-bottom (e.g., '┴')
     * @param leftT Tee-left (e.g., '├')
     * @param rightT Tee-right (e.g., '┤')
     * @param cross Cross (e.g., '┼')
     */
    public BoxCharset(
            char horizontal,  char vertical,
            char topLeft, char topRight, char bottomLeft, char bottomRight,
            char topT, char bottomT, char leftT, char rightT,
            char cross
    ) {
        this.chars = new char[16];
        Arrays.fill(this.chars, ' '); // Default all to empty/space

        // --- Set the 11 main characters based on their *connections* ---

        // 2 connections
        this.chars[LEFT | RIGHT]   = vertical;  // 12
        this.chars[TOP | BOTTOM]   = horizontal;  // 3
        this.chars[BOTTOM | RIGHT] = topLeft; // 10 (Connects to Bottom and Right)
        this.chars[BOTTOM | LEFT]  = topRight; // 6  (Connects to Bottom and Left)
        this.chars[TOP | RIGHT]    = bottomLeft; // 9  (Connects to Top and Right)
        this.chars[TOP | LEFT]     = bottomRight; // 5  (Connects to Top and Left)

        // 3 connections
        this.chars[BOTTOM | LEFT | RIGHT] = topT;    // 14
        this.chars[TOP | LEFT | RIGHT]    = bottomT;    // 13
        this.chars[TOP | BOTTOM | RIGHT]  = leftT; // 11
        this.chars[TOP | BOTTOM | LEFT]   = rightT;// 7

        // 4 connections
        this.chars[TOP | BOTTOM | LEFT | RIGHT] = cross; // 15

        // --- Set defaults for the 4 single-connection "end-caps" ---
        // These can be overridden later with setChar()
        this.chars[TOP]    = '╵'; // 1
        this.chars[BOTTOM] = '╷'; // 2
        this.chars[LEFT]   = '╴'; // 4
        this.chars[RIGHT]  = '╶'; // 8
        // index 0 is already ' ' (space)
    }

    /**
     * Gets the character for a specific combination of connections.
     *
     * @param top    True if a connection to the top is needed.
     * @param bottom True if a connection to the bottom is needed.
     * @param left   True if a connection to the left is needed.
     * @param right  True if a connection to the right is needed.
     * @return The corresponding box-drawing character.
     */
    public char getChar(boolean top, boolean bottom, boolean left, boolean right) {
        int index = 0;
        if (top)    index |= TOP;
        if (bottom) index |= BOTTOM;
        if (left)   index |= LEFT;
        if (right)  index |= RIGHT;
        return this.chars[index];
    }

    /**
     * Directly sets a character at a specific bitmask index.
     * Useful for customizing the end-caps or empty space.
     *
     * @param index The bitmask index (0-15).
     * @param c     The character to set.
     */
    public void setChar(int index, char c) {
        if (index >= 0 && index < 16) {
            this.chars[index] = c;
        }
    }

    /**
     * Directly sets a character for a specific combination of connections.
     *
     * @param c      The character to set.
     * @param top    True if a connection to the top is needed.
     * @param bottom True if a connection to the bottom is needed.
     * @param left   True if a connection to the left is needed.
     * @param right  True if a connection to the right is needed.
     */
    public void setChar(char c, boolean top, boolean bottom, boolean left, boolean right) {
        int index = 0;
        if (top)    index |= TOP;
        if (bottom) index |= BOTTOM;
        if (left)   index |= LEFT;
        if (right)  index |= RIGHT;
        setChar(index, c);
    }

    // --- ========================== ---
    // ---   STATIC DEFAULT CHARSETS  ---
    // --- ========================== ---

    /**
     * Standard single-line box-drawing characters.
     * ┌───┐
     * │   │
     * └───┘
     */
    public static final BoxCharset SINGLE;

    /**
     * Double-line box-drawing characters.
     * ╔═══╗
     * ║   ║
     * ╚═══╝
     */
    public static final BoxCharset DOUBLE;

    /**
     * Single-line characters with rounded corners.
     * ╭───╮
     * │   │
     * ╰───╯
     */
    public static final BoxCharset ROUNDED;

    /**
     * Simple ASCII characters.
     * +---+
     * |   |
     * +---+
     */
    public static final BoxCharset ASCII;

    /**
     * Heavy (bold) single-line characters.
     * ┏━━━┓
     * ┃   ┃
     * ┗━━━┛
     */
    public static final BoxCharset HEAVY;

    // Static initializer block to create the default instances
    static {
        // Standard single-line
        SINGLE = new BoxCharset(
                '─', '│', '┌', '┐', '└', '┘', '┬', '┴', '├', '┤', '┼'
        );

        // Double-line
        DOUBLE = new BoxCharset(
                '═', '║', '╔', '╗', '╚', '╝', '╦', '╩', '╠', '╣', '╬'
        );
        // Override default end-caps with double-line style
        DOUBLE.setChar(TOP, '╹');
        DOUBLE.setChar(BOTTOM, '╻');
        DOUBLE.setChar(LEFT, '╸');
        DOUBLE.setChar(RIGHT, '╺');

        // Rounded corners
        // Note: Tees and cross use the single-line versions as no rounded
        // versions exist in standard Unicode.
        ROUNDED = new BoxCharset(
                '─', '│', '╭', '╮', '╰', '╯', '┬', '┴', '├', '┤', '┼'
        );

        // ASCII
        ASCII = new BoxCharset(
                '-', '|', '+', '+', '+', '+', '+', '+', '+', '+', '+'
        );
        // Override end-caps for ASCII
        ASCII.setChar(TOP, '^');
        ASCII.setChar(BOTTOM, 'v');
        ASCII.setChar(LEFT, '<');
        ASCII.setChar(RIGHT, '>');

        // Heavy (Bold)
        HEAVY = new BoxCharset(
                '━', '┃', '┏', '┓', '┗', '┛', '┳', '┻', '┣', '┫', '╋'
        );
        // Override end-caps
        HEAVY.setChar(TOP, '╹');
        HEAVY.setChar(BOTTOM, '╻');
        HEAVY.setChar(LEFT, '╸');
        HEAVY.setChar(RIGHT, '╺');
    }
}