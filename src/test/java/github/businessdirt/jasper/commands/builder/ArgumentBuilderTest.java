/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands.builder;

import static bollschweiler.de.lmu.ifi.cip.gitlab2.commands.builder.LiteralArgumentBuilder.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.arguments.IntegerArgumentType;
import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.arguments.StringArgumentType;
import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree.ArgumentCommandNode;
import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree.CommandNode;
import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree.LiteralCommandNode;
import org.junit.jupiter.api.Test;

class ArgumentBuilderTest {

    @Test
    void testBasicLiteral() {
        LiteralCommandNode node = literal("test").build();
        assertEquals("test", node.getName());
    }

    @Test
    void testLiteralWithExecutor() {
        LiteralCommandNode node = literal("test").executes(c -> 0).build();
        assertNotNull(node.getCommand());
    }

    @Test
    void testChainedLiterals() {
        LiteralCommandNode node = literal("a")
                .literal("b", b -> b.executes(c -> 0))
                .build();
        assertEquals(1, node.getChildren().size());
        CommandNode child = node.getChildren().get("b");
        assertNotNull(child);
        assertEquals("b", child.getName());
        assertNotNull(child.getCommand());
    }

    @Test
    void testLiteralWithArgument() {
        LiteralCommandNode node = literal("set")
                .argument("value", new IntegerArgumentType(), value -> value.executes(c -> 0))
                .build();
        assertEquals(1, node.getChildren().size());
        CommandNode child = node.getChildren().get("value");
        assertNotNull(child);
        assertInstanceOf(ArgumentCommandNode.class, child);
        assertEquals("value", child.getName());
        assertNotNull(child.getCommand());
        assertInstanceOf(IntegerArgumentType.class, ((ArgumentCommandNode<?>) child).getType());
    }

    @Test
    void testComplexStructure() {
        LiteralCommandNode node = literal("a")
                .literal("b", b -> b
                        .argument("c", new StringArgumentType(), c -> c.executes(cmd -> 0))
                )
                .argument("d", new IntegerArgumentType(), d -> d.executes(cmd -> 1))
                .build();

        assertEquals(2, node.getChildren().size());
        assertTrue(node.getChildren().containsKey("b"));
        assertTrue(node.getChildren().containsKey("d"));

        CommandNode b = node.getChildren().get("b");
        assertNull(b.getCommand());
        assertEquals(1, b.getChildren().size());
        assertTrue(b.getChildren().containsKey("c"));
        CommandNode c = b.getChildren().get("c");
        assertNotNull(c.getCommand());

        CommandNode d = node.getChildren().get("d");
        assertNotNull(d.getCommand());
    }
}
