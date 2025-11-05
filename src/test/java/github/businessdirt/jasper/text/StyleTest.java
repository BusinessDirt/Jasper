package github.businessdirt.jasper.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StyleTest {

    @Test
    @DisplayName("Should create a new style with the specified color")
    void testWithColor() {
        Style style = Style.EMPTY.withColor(TextColor.fromRgb(255, 0, 0));
        assertEquals(TextColor.fromRgb(255, 0, 0), style.color());
    }

    @Test
    @DisplayName("Should create a new style with bold set to true")
    void testWithBold() {
        Style style = Style.EMPTY.withBold(true);
        assertTrue(style.bold());
    }

    @Test
    @DisplayName("Should create a new style with italic set to true")
    void testWithItalic() {
        Style style = Style.EMPTY.withItalic(true);
        assertTrue(style.italic());
    }

    @Test
    @DisplayName("Should create a new style with underlined set to true")
    void testWithUnderlined() {
        Style style = Style.EMPTY.withUnderlined(true);
        assertTrue(style.underlined());
    }

    @Test
    @DisplayName("Should create a new style with strikethrough set to true")
    void testWithStrikethrough() {
        Style style = Style.EMPTY.withStrikethrough(true);
        assertTrue(style.strikethrough());
    }

    @Test
    @DisplayName("Should merge two styles correctly")
    void testMerge() {
        Style style1 = new Style(TextColor.fromRgb(255, 0, 0), true, false, false, false);
        Style style2 = new Style(TextColor.fromRgb(0, 255, 0), false, true, true, false);
        Style merged = style1.merge(style2);

        assertEquals(TextColor.fromRgb(0, 255, 0), merged.color());
        assertTrue(merged.bold());
        assertTrue(merged.italic());
        assertTrue(merged.underlined());
        assertFalse(merged.strikethrough());
    }

    @Test
    @DisplayName("Should return the other style when merging with an empty style")
    void testMergeWithEmpty() {
        Style style = new Style(TextColor.fromRgb(255, 0, 0), true, false, false, false);
        Style merged = Style.EMPTY.merge(style);

        assertEquals(style, merged);
    }
}