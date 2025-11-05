/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StringReaderTest {

    @Test
    void testReadString() {
        StringReader reader = new StringReader("hello world");
        assertEquals("hello", reader.readString());
        assertEquals(5, reader.getCursor());
    }

    @Test
    void testReadRemaining() {
        StringReader reader = new StringReader("hello world");
        reader.setCursor(6);
        assertEquals("world", reader.readRemaining());
        assertEquals(11, reader.getCursor());
    }

    @Test
    void testCanRead() {
        StringReader reader = new StringReader("hello");
        assertTrue(reader.canRead());
        reader.setCursor(5);
        assertFalse(reader.canRead());
    }

    @Test
    void testCanReadWithLength() {
        StringReader reader = new StringReader("hello");
        assertTrue(reader.canRead(5));
        assertFalse(reader.canRead(6));
    }

    @Test
    void testPeek() {
        StringReader reader = new StringReader("hello");
        assertEquals('h', reader.peek());
        reader.skip();
        assertEquals('e', reader.peek());
    }

    @Test
    void testSkip() {
        StringReader reader = new StringReader("hello");
        reader.skip();
        assertEquals(1, reader.getCursor());
    }

    @Test
    void testConstructorCopy() {
        StringReader original = new StringReader("hello world");
        original.setCursor(5);
        StringReader copy = new StringReader(original);
        assertEquals(original.getCursor(), copy.getCursor());
        assertEquals(" world", copy.readRemaining());
    }
}
