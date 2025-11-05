/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommandContextTest {

    private CommandContext<TestCommandSource> commandContext;

    @BeforeEach
    void setUp() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("arg1", "value1");
        arguments.put("arg2", 123);

        commandContext = new CommandContext<>(null, arguments);
    }

    @Test
    @DisplayName("Should return the source")
    void getSource() {
        assertNull(commandContext.source());
    }

    @Test
    @DisplayName("Should return the correct argument")
    void getArgument() {
        assertEquals("value1", commandContext.getArgument("arg1", String.class));
        assertEquals(123, commandContext.getArgument("arg2", Integer.class));
    }

    @Test
    @DisplayName("Should return null for a non-existent argument")
    void getArgument_NotFound() {
        assertNull(commandContext.getArgument("nonexistent", String.class));
    }

    @Test
    @DisplayName("Should throw ClassCastException for an argument of the wrong type")
    void getArgument_WrongType() {
        // This will throw a ClassCastException at runtime, which is expected behavior.
        // The @SuppressWarnings("unchecked") in the original code indicates this.
        assertThrows(ClassCastException.class, () -> {
            Integer value = commandContext.getArgument("arg1", Integer.class);
        });
    }
}