/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandContextTest {

    private CommandContext commandContext;

    @BeforeEach
    void setUp() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("arg1", "value1");
        arguments.put("arg2", 123);

        commandContext = new CommandContext(null, arguments);
    }

    @Test
    void getSource() {
        assertNull(commandContext.source());
    }

    @Test
    void getArgument() {
        assertEquals("value1", commandContext.getArgument("arg1", String.class));
        assertEquals(123, commandContext.getArgument("arg2", Integer.class));
    }

    @Test
    void getArgument_NotFound() {
        assertNull(commandContext.getArgument("nonexistent", String.class));
    }

    @Test
    void getArgument_WrongType() {
        // This will throw a ClassCastException at runtime, which is expected behavior.
        // The @SuppressWarnings("unchecked") in the original code indicates this.
        assertThrows(ClassCastException.class, () -> {
            Integer value = commandContext.getArgument("arg1", Integer.class);
        });
    }
}
