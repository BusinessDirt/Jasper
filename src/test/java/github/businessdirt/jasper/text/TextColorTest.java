package github.businessdirt.jasper.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

class TextColorTest {

    @Test
    @DisplayName("Should create TextColor from RGB integer")
    void testFromRgb() {
        TextColor color = TextColor.fromRgb(255, 0, 0);
        assertEquals(0xFFFF0000, color.rgb());
    }

    @Test
    @DisplayName("Should create TextColor from java.awt.Color")
    void testFromColor() {
        Color awtColor = new Color(0x00FF00);
        TextColor color = TextColor.fromColor(awtColor);
        assertEquals(0xFF00FF00, color.rgb());
    }

    @Test
    @DisplayName("Should correctly compare two TextColor objects")
    void testEquals() {
        TextColor color1 = TextColor.fromRgb(255, 255, 255);
        TextColor color2 = TextColor.fromRgb(255, 255, 255);
        TextColor color3 = TextColor.fromRgb(0, 0, 0);

        assertEquals(color1, color2);
        assertNotEquals(color1, color3);
    }
}