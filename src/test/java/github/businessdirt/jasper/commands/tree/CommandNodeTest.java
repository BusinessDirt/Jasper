/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.Command;
import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.arguments.IntegerArgumentType;
import org.junit.jupiter.api.Test;

class CommandNodeTest {

    @Test
    void literalCommandNode() {
        LiteralCommandNode node = new LiteralCommandNode("test");
        assertEquals("test", node.getName());
    }

    @Test
    void argumentCommandNode() {
        ArgumentCommandNode<Integer> node = new ArgumentCommandNode<>("amount", new IntegerArgumentType());
        assertEquals("amount", node.getName());
        assertNotNull(node.getType());
    }

    @Test
    void rootCommandNode() {
        RootCommandNode node = new RootCommandNode();
        assertEquals("", node.getName());
    }

    @Test
    void addChild() {
        CommandNode root = new RootCommandNode();
        CommandNode child = new LiteralCommandNode("child");
        root.addChild(child);
        assertTrue(root.getChildren().containsKey("child"));
        assertEquals(child, root.getChildren().get("child"));
    }

    @Test
    void executes() {
        CommandNode node = new LiteralCommandNode("test");
        Command command = context -> 1;
        node.executes(command);
        assertEquals(command, node.getCommand());
    }

    @Test
    void getUsage_literal() {
        LiteralCommandNode node = new LiteralCommandNode("test");
        assertEquals("/test", node.getUsage());
    }

    @Test
    void getUsage_literalWithChild() {
        LiteralCommandNode root = new LiteralCommandNode("root");
        root.addChild(new LiteralCommandNode("child"));
        assertEquals("/root child", root.getUsage());
    }
    
    @Test
    void getUsage_literalWithMultipleChildren() {
        LiteralCommandNode root = new LiteralCommandNode("root");
        root.addChild(new LiteralCommandNode("child1"));
        root.addChild(new LiteralCommandNode("child2"));
        assertEquals("/root [child1|child2]", root.getUsage());
    }

    @Test
    void getUsage_argument() {
        LiteralCommandNode root = new LiteralCommandNode("say");
        root.addChild(new ArgumentCommandNode<>("message", null)); // type is not used in getUsage
        assertEquals("/say <message>", root.getUsage());
    }
}
