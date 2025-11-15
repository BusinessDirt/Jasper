package github.businessdirt.jasper.commands;

import github.businessdirt.jasper.commands.tree.CommandNode;
import github.businessdirt.jasper.commands.tree.LiteralCommandNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelpCommandTest {

    private ByteArrayOutputStream outContent;
    private TestCommandSource source;

    @BeforeEach
    public void setUp() {
        this.outContent = new ByteArrayOutputStream();
        this.source = new TestCommandSource(new PrintStream(this.outContent));
    }

    @Test
    @DisplayName("Should display the first page of help")
    void run_firstPage() {
        Map<String, CommandNode<TestCommandSource>> commands = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            commands.put("cmd" + i, new LiteralCommandNode<>("cmd" + i));
        }

        HelpCommand<TestCommandSource> helpCommand = new HelpCommand<>(commands);

        Map<String, Object> args = new HashMap<>();
        CommandContext<TestCommandSource> context = new CommandContext<>(this.source, args); // page is null

        helpCommand.run(context);

        String output = this.outContent.toString();
        assertTrue(output.contains("Showing help page 1 of 2"));
        assertTrue(output.contains("/cmd0"));
        assertTrue(output.contains("/cmd4"));
        assertFalse(output.contains("/cmd5"));
    }

    @Test
    @DisplayName("Should display the second page of help")
    void run_secondPage() {
        Map<String, CommandNode<TestCommandSource>> commands = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            commands.put("cmd" + i, new LiteralCommandNode<>("cmd" + i));
        }

        HelpCommand<TestCommandSource> helpCommand = new HelpCommand<>(commands);

        Map<String, Object> args = new HashMap<>();
        args.put("page", 2);
        CommandContext<TestCommandSource> context = new CommandContext<>(this.source, args);

        helpCommand.run(context);

        String output = this.outContent.toString();
        assertTrue(output.contains("Showing help page 2 of 2"));
        assertFalse(output.contains("/cmd4"));
        assertTrue(output.contains("/cmd5"));
        assertTrue(output.contains("/cmd6"));
    }

    @Test
    @DisplayName("Should handle a page number that is out of bounds")
    void run_pageOutOfBounds() {
        Map<String, CommandNode<TestCommandSource>> commands = new LinkedHashMap<>();
        commands.put("cmd1", new LiteralCommandNode<>("cmd1"));

        HelpCommand<TestCommandSource> helpCommand = new HelpCommand<>(commands);

        Map<String, Object> args = new HashMap<>();
        args.put("page", 100);
        CommandContext<TestCommandSource> context = new CommandContext<>(this.source, args);

        helpCommand.run(context);

        String output = this.outContent.toString();
        assertTrue(output.contains("Showing help page 1 of 1"));
        assertTrue(output.contains("/cmd1"));
    }

    @Test
    @DisplayName("Should handle a page number of zero")
    void run_pageZero() {
        Map<String, CommandNode<TestCommandSource>> commands = new LinkedHashMap<>();
        commands.put("cmd1", new LiteralCommandNode<>("cmd1"));

        HelpCommand<TestCommandSource> helpCommand = new HelpCommand<>(commands);

        Map<String, Object> args = new HashMap<>();
        args.put("page", 0);
        CommandContext<TestCommandSource> context = new CommandContext<>(this.source, args);

        helpCommand.run(context);

        String output = this.outContent.toString();
        assertTrue(output.contains("Showing help page 1 of 1"));
    }
}