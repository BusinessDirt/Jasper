/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.StringReader;
import org.junit.jupiter.api.Test;

class StringArgumentTypeTest {

    @Test
    void parse() {
        StringArgumentType type = new StringArgumentType();
        StringReader reader = new StringReader("hello world");
        assertEquals("hello", type.parse(reader));
    }
}
