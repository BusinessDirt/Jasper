package github.businessdirt.jasper.commands.builder;

import github.businessdirt.jasper.commands.CommandResult;
import github.businessdirt.jasper.commands.TestCommandSource;
import github.businessdirt.jasper.commands.arguments.IntegerArgumentType;
import github.businessdirt.jasper.commands.arguments.StringArgumentType;
import github.businessdirt.jasper.commands.tree.ArgumentCommandNode;
import github.businessdirt.jasper.commands.tree.CommandNode;
import github.businessdirt.jasper.commands.tree.LiteralCommandNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentBuilderTest {

    @Test
    @DisplayName("Should build a basic literal command node")
    void testBasicLiteral() {
        LiteralCommandNode<TestCommandSource> node = LiteralArgumentBuilder.<TestCommandSource>literal("test").build();
        assertEquals("test", node.getName());
    }

    @Test
    @DisplayName("Should build a literal command node with an executor")
    void testLiteralWithExecutor() {
        LiteralCommandNode<TestCommandSource> node = LiteralArgumentBuilder.<TestCommandSource>literal("test")
                .executes(_ -> CommandResult.SUCCESS_STATUS)
                .build();

        assertNotNull(node.getCommand());
    }

    @Test
    @DisplayName("Should build chained literal command nodes")
    void testChainedLiterals() {
        LiteralCommandNode<TestCommandSource> node = LiteralArgumentBuilder.<TestCommandSource>literal("a")
                .literal("b", b ->
                        b.executes(_ -> CommandResult.SUCCESS_STATUS))
                .build();
        assertEquals(1, node.getChildren().size());
        CommandNode<TestCommandSource> child = node.getChildren().get("b");
        assertNotNull(child);
        assertEquals("b", child.getName());
        assertNotNull(child.getCommand());
    }

    @Test
    @DisplayName("Should build a literal command node with an argument")
    void testLiteralWithArgument() {
        LiteralCommandNode<TestCommandSource> node = LiteralArgumentBuilder.<TestCommandSource>literal("set")
                .argument("value", new IntegerArgumentType(), value ->
                        value.executes(_ -> CommandResult.SUCCESS_STATUS))
                .build();

        assertEquals(1, node.getChildren().size());
        CommandNode<TestCommandSource> child = node.getChildren().get("value");
        assertNotNull(child);
        assertInstanceOf(ArgumentCommandNode.class, child);
        assertEquals("value", child.getName());
        assertNotNull(child.getCommand());
        assertInstanceOf(IntegerArgumentType.class, ((ArgumentCommandNode<TestCommandSource, ?>) child).getType());
    }

    @Test
    @DisplayName("Should build a complex command structure")
    void testComplexStructure() {
        LiteralCommandNode<TestCommandSource> node = LiteralArgumentBuilder.<TestCommandSource>literal("a")
                .literal("b", b ->
                        b.argument("c", new StringArgumentType(), c ->
                                c.executes(_ -> CommandResult.SUCCESS_STATUS)))
                .argument("d", new IntegerArgumentType(), d ->
                    d.executes(_ -> CommandResult.SUCCESS_STATUS))
                .build();

        assertEquals(2, node.getChildren().size());
        assertTrue(node.getChildren().containsKey("b"));
        assertTrue(node.getChildren().containsKey("d"));

        CommandNode<TestCommandSource> b = node.getChildren().get("b");
        assertNull(b.getCommand());
        assertEquals(1, b.getChildren().size());
        assertTrue(b.getChildren().containsKey("c"));
        CommandNode<TestCommandSource> c = b.getChildren().get("c");
        assertNotNull(c.getCommand());

        CommandNode<TestCommandSource> d = node.getChildren().get("d");
        assertNotNull(d.getCommand());
    }
}