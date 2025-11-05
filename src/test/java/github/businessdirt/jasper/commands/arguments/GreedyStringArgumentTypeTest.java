/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.StringReader;
import org.junit.jupiter.api.Test;

class GreedyStringArgumentTypeTest {

    @Test
    void parse() {
        GreedyStringArgumentType type = new GreedyStringArgumentType();
        StringReader reader = new StringReader("hello world");
        reader.setCursor(6);
        assertEquals("world", type.parse(reader));
    }

    @Test
    void parse_readsRemaining() {
        GreedyStringArgumentType type = new GreedyStringArgumentType();
        StringReader reader = new StringReader("this is a long string");
        assertEquals("this is a long string", type.parse(reader));
    }
}
