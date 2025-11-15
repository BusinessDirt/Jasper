package github.businessdirt.jasper.commands.arguments;

import github.businessdirt.jasper.commands.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntegerArgumentTypeTest {

    @Test
    @DisplayName("Should parse a valid integer")
    void parse() {
        IntegerArgumentType type = new IntegerArgumentType();
        StringReader reader = new StringReader("123");
        assertEquals(123, type.parse(reader));
    }

    @Test
    @DisplayName("Should throw NumberFormatException for an invalid integer")
    void parse_invalid() {
        IntegerArgumentType type = new IntegerArgumentType();
        StringReader reader = new StringReader("abc");
        assertThrows(NumberFormatException.class, () -> type.parse(reader));
    }
}