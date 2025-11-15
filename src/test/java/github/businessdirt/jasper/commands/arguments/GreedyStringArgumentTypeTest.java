package github.businessdirt.jasper.commands.arguments;

import github.businessdirt.jasper.commands.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GreedyStringArgumentTypeTest {

    @Test
    @DisplayName("Should parse the remaining string")
    void parse() {
        GreedyStringArgumentType type = new GreedyStringArgumentType();
        StringReader reader = new StringReader("hello world");
        reader.setCursor(6);
        assertEquals("world", type.parse(reader));
    }

    @Test
    @DisplayName("Should parse the entire remaining string")
    void parse_readsRemaining() {
        GreedyStringArgumentType type = new GreedyStringArgumentType();
        StringReader reader = new StringReader("this is a long string");
        assertEquals("this is a long string", type.parse(reader));
    }
}