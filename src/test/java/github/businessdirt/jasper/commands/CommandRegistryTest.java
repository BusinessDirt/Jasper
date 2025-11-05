/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CommandRegistryTest {

    @Test
    void getInstance() {
        assertNotNull(CommandRegistry.getInstance());
        assertSame(CommandRegistry.getInstance(), CommandRegistry.getInstance());
    }

    @Test
    void isMessageCommand() {
        assertTrue(CommandRegistry.isMessageCommand("/hello"));
        assertTrue(CommandRegistry.isMessageCommand("bye"));
        assertTrue(CommandRegistry.isMessageCommand("BYE"));
        assertFalse(CommandRegistry.isMessageCommand("just a message"));
    }

    @Test
    void handle_helpCommand() {
        CommandRegistry registry = CommandRegistry.getInstance();
        // The handle method returns true if the client should continue, false otherwise.
        // For "help", it should return true. And not throw an exception.
        assertTrue(registry.handle(null, "/help"));
    }
    
    @Test
    void handle_stripsSlash() {
        CommandRegistry registry = CommandRegistry.getInstance();
        assertTrue(registry.handle(null, "help"));
    }

    @Test
    void handle_unknownCommand() {
        CommandRegistry registry = CommandRegistry.getInstance();
        // An unknown command will cause an exception inside handle, which is caught, logged,
        // and then handle returns true.
        assertTrue(registry.handle(null, "/unknowncommand"));
    }
}
