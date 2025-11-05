/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandRegistryTest {

    @Test
    @DisplayName("Should get the same registry instance for the same class")
    void get() {
        assertNotNull(CommandRegistry.get(TestCommandSource.class));
        assertEquals(CommandRegistry.get(TestCommandSource.class), CommandRegistry.get(TestCommandSource.class));
    }

    @Test
    @DisplayName("Should correctly identify a message command")
    void isMessageCommand() {
        assertTrue(CommandRegistry.isMessageCommand("/hello"));
        assertFalse(CommandRegistry.isMessageCommand("just a message"));
    }

    @Test
    @DisplayName("Should handle the help command")
    void handle_helpCommand() {
        CommandRegistry<TestCommandSource> registry = CommandRegistry.get(TestCommandSource.class);
        assertEquals(CommandResult.SUCCESS, registry.handle(null, "/help"));
    }
    
    @Test
    @DisplayName("Should handle a command with a leading slash stripped")
    void handle_stripsSlash() {
        CommandRegistry<TestCommandSource> registry = CommandRegistry.get(TestCommandSource.class);
        assertEquals(CommandResult.SUCCESS, registry.handle(null, "help"));
    }

    @Test
    @DisplayName("Should handle an unknown command")
    void handle_unknownCommand() {
        CommandRegistry<TestCommandSource> registry = CommandRegistry.get(TestCommandSource.class);
        assertEquals(CommandResult.UNKNOWN_COMMAND, registry.handle(null, "/unknowncommand"));
    }
}