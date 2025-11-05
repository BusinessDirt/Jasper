/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree.CommandNode;
import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree.LiteralCommandNode;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelpCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void run_firstPage() {
        Map<String, CommandNode> commands = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            commands.put("cmd" + i, new LiteralCommandNode("cmd" + i));
        }
        HelpCommand helpCommand = new HelpCommand(commands);
        Map<String, Object> args = new HashMap<>();
        CommandContext context = new CommandContext(null, args); // page is null

        helpCommand.run(context);

        String output = outContent.toString();
        assertTrue(output.contains("Showing help page 1 of 2"));
        assertTrue(output.contains("/cmd0"));
        assertTrue(output.contains("/cmd4"));
        assertFalse(output.contains("/cmd5"));
    }

    @Test
    void run_secondPage() {
        Map<String, CommandNode> commands = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            commands.put("cmd" + i, new LiteralCommandNode("cmd" + i));
        }
        HelpCommand helpCommand = new HelpCommand(commands);
        Map<String, Object> args = new HashMap<>();
        args.put("page", 2);
        CommandContext context = new CommandContext(null, args);

        helpCommand.run(context);

        String output = outContent.toString();
        assertTrue(output.contains("Showing help page 2 of 2"));
        assertFalse(output.contains("/cmd4"));
        assertTrue(output.contains("/cmd5"));
        assertTrue(output.contains("/cmd6"));
    }

    @Test
    void run_pageOutOfBounds() {
        Map<String, CommandNode> commands = new LinkedHashMap<>();
        commands.put("cmd1", new LiteralCommandNode("cmd1"));
        HelpCommand helpCommand = new HelpCommand(commands);
        Map<String, Object> args = new HashMap<>();
        args.put("page", 100);
        CommandContext context = new CommandContext(null, args);

        helpCommand.run(context);

        String output = outContent.toString();
        assertTrue(output.contains("Showing help page 1 of 1"));
        assertTrue(output.contains("/cmd1"));
    }

    @Test
    void run_pageZero() {
        Map<String, CommandNode> commands = new LinkedHashMap<>();
        commands.put("cmd1", new LiteralCommandNode("cmd1"));
        HelpCommand helpCommand = new HelpCommand(commands);
        Map<String, Object> args = new HashMap<>();
        args.put("page", 0);
        CommandContext context = new CommandContext(null, args);

        helpCommand.run(context);

        String output = outContent.toString();
        assertTrue(output.contains("Showing help page 1 of 1"));
    }
}
