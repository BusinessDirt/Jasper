/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.StringReader;
import org.junit.jupiter.api.Test;

class IntegerArgumentTypeTest {

    @Test
    void parse() {
        IntegerArgumentType type = new IntegerArgumentType();
        StringReader reader = new StringReader("123");
        assertEquals(123, type.parse(reader));
    }

    @Test
    void parse_invalid() {
        IntegerArgumentType type = new IntegerArgumentType();
        StringReader reader = new StringReader("abc");
        assertThrows(NumberFormatException.class, () -> type.parse(reader));
    }
}
