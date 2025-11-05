/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringReaderTest {

    @Test
    @DisplayName("Should read a string until a space")
    void testReadString() {
        StringReader reader = new StringReader("hello world");
        assertEquals("hello", reader.readString());
        assertEquals(5, reader.getCursor());
    }

    @Test
    @DisplayName("Should read the remaining part of the string")
    void testReadRemaining() {
        StringReader reader = new StringReader("hello world");
        reader.setCursor(6);
        assertEquals("world", reader.readRemaining());
        assertEquals(11, reader.getCursor());
    }

    @Test
    @DisplayName("Should correctly report if it can read more")
    void testCanRead() {
        StringReader reader = new StringReader("hello");
        assertTrue(reader.canRead());
        reader.setCursor(5);
        assertFalse(reader.canRead());
    }

    @Test
    @DisplayName("Should correctly report if it can read a certain length")
    void testCanReadWithLength() {
        StringReader reader = new StringReader("hello");
        assertTrue(reader.canRead(5));
        assertFalse(reader.canRead(6));
    }

    @Test
    @DisplayName("Should peek at the next character without consuming it")
    void testPeek() {
        StringReader reader = new StringReader("hello");
        assertEquals('h', reader.peek());
        reader.skip();
        assertEquals('e', reader.peek());
    }

    @Test
    @DisplayName("Should skip a character")
    void testSkip() {
        StringReader reader = new StringReader("hello");
        reader.skip();
        assertEquals(1, reader.getCursor());
    }

    @Test
    @DisplayName("Should copy a StringReader correctly")
    void testConstructorCopy() {
        StringReader original = new StringReader("hello world");
        original.setCursor(5);
        StringReader copy = new StringReader(original);
        assertEquals(original.getCursor(), copy.getCursor());
        assertEquals(" world", copy.readRemaining());
    }
}