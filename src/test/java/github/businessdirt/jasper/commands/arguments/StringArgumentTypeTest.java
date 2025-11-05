/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands.arguments;

import github.businessdirt.jasper.commands.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringArgumentTypeTest {

    @Test
    @DisplayName("Should parse a single word string")
    void parse() {
        StringArgumentType type = new StringArgumentType();
        StringReader reader = new StringReader("hello world");
        assertEquals("hello", type.parse(reader));
    }
}