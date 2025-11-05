package github.businessdirt.jasper.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LiteralTextTest {

    @Test
    @DisplayName("Should return the correct string")
    void testAsString() {
        LiteralText text = new LiteralText("Hello, world!");
        assertEquals("Hello, world!", text.asString());
    }

    @Test
    @DisplayName("Should append a text component")
    void testAppend() {
        LiteralText text1 = new LiteralText("Hello, ");
        LiteralText text2 = new LiteralText("world!");
        text1.append(text2);

        assertEquals(1, text1.getSiblings().size());
        assertEquals(text2, text1.getSiblings().getFirst());
    }
}