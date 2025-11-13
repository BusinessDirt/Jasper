/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import github.businessdirt.jasper.events.system.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CommandRegistryTest {

    private TestCommandSource source;

    @BeforeEach
    void setUp() {
        this.source = new TestCommandSource(new PrintStream(new ByteArrayOutputStream()));

        Logger logger = LogManager.getLogger();
        EventBus.initialize("github.businessdirt.jasper", logger);
        CommandRegistry.initialize("github.businessdirt.jasper", logger);
    }

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
        CommandResult result = CommandRegistry.handle(TestCommandSource.class, this.source, "/help");
        assertEquals(CommandResult.SUCCESS, result);
    }
    
    @Test
    @DisplayName("Should handle a command with a leading slash stripped")
    void handle_stripsSlash() {
        CommandResult result = CommandRegistry.handle(TestCommandSource.class, this.source, "help");
        assertEquals(CommandResult.SUCCESS, result);
    }

    @Test
    @DisplayName("Should handle an unknown command")
    void handle_unknownCommand() {
        CommandResult result = CommandRegistry.handle(TestCommandSource.class, this.source, "/unknowncommand");
        assertEquals(CommandResult.UNKNOWN_COMMAND, result);
    }
}