/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands.tree;

import github.businessdirt.jasper.commands.Command;
import github.businessdirt.jasper.commands.CommandResult;
import github.businessdirt.jasper.commands.TestCommandSource;
import github.businessdirt.jasper.commands.arguments.IntegerArgumentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandNodeTest {

    @Test
    @DisplayName("Should create a literal command node")
    void literalCommandNode() {
        LiteralCommandNode<TestCommandSource> node = new LiteralCommandNode<>("test");
        assertEquals("test", node.getName());
    }

    @Test
    @DisplayName("Should create an argument command node")
    void argumentCommandNode() {
        ArgumentCommandNode<TestCommandSource, Integer> node = new ArgumentCommandNode<>("amount", new IntegerArgumentType());
        assertEquals("amount", node.getName());
        assertNotNull(node.getType());
    }

    @Test
    @DisplayName("Should create a root command node")
    void rootCommandNode() {
        RootCommandNode<TestCommandSource> node = new RootCommandNode<>();
        assertEquals("", node.getName());
    }

    @Test
    @DisplayName("Should add a child node")
    void addChild() {
        CommandNode<TestCommandSource> root = new RootCommandNode<>();
        CommandNode<TestCommandSource> child = new LiteralCommandNode<>("child");
        root.addChild(child);
        assertTrue(root.getChildren().containsKey("child"));
        assertEquals(child, root.getChildren().get("child"));
    }

    @Test
    @DisplayName("Should set the command to be executed")
    void executes() {
        CommandNode<TestCommandSource> node = new LiteralCommandNode<>("test");
        Command<TestCommandSource> command = context -> CommandResult.SUCCESS;
        node.executes(command);
        assertEquals(command, node.getCommand());
    }

    @Test
    @DisplayName("Should get the usage for a literal node")
    void getUsage_literal() {
        LiteralCommandNode<TestCommandSource> node = new LiteralCommandNode<>("test");
        assertEquals("/test", node.getUsage());
    }

    @Test
    @DisplayName("Should get the usage for a literal node with a child")
    void getUsage_literalWithChild() {
        LiteralCommandNode<TestCommandSource> root = new LiteralCommandNode<>("root");
        root.addChild(new LiteralCommandNode<>("child"));
        assertEquals("/root child", root.getUsage());
    }
    
    @Test
    @DisplayName("Should get the usage for a literal node with multiple children")
    void getUsage_literalWithMultipleChildren() {
        LiteralCommandNode<TestCommandSource> root = new LiteralCommandNode<>("root");
        root.addChild(new LiteralCommandNode<>("child1"));
        root.addChild(new LiteralCommandNode<>("child2"));
        assertEquals("/root [child1|child2]", root.getUsage());
    }

    @Test
    @DisplayName("Should get the usage for a node with an argument")
    void getUsage_argument() {
        LiteralCommandNode<TestCommandSource> root = new LiteralCommandNode<>("say");
        root.addChild(new ArgumentCommandNode<>("message", null)); // type is not used in getUsage
        assertEquals("/say <message>", root.getUsage());
    }
}