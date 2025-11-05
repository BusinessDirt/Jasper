/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import github.businessdirt.jasper.commands.arguments.IntegerArgumentType;
import github.businessdirt.jasper.commands.builder.LiteralArgumentBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandDispatcherTest {

    private CommandDispatcher<TestCommandSource> dispatcher;
    private TestCommandSource source;

    @BeforeEach
    void setUp() {
        dispatcher = new CommandDispatcher<>();
        source = new TestCommandSource();
    }

    @Test
    @DisplayName("Should execute a simple literal command")
    void testSimpleLiteralCommand() {
        AtomicBoolean executed = new AtomicBoolean(false);
        dispatcher.register(LiteralArgumentBuilder.<TestCommandSource>literal("test").executes(c -> {
            executed.set(true);
            return CommandResult.SUCCESS;
        }));

        CommandResult result = dispatcher.execute("test", source);
        assertTrue(executed.get());
        assertEquals(CommandResult.SUCCESS_STATUS, result.status());
    }

    @Test
    @DisplayName("Should return unknown command for an unknown command")
    void testUnknownCommand() {
        assertEquals(CommandResult.UNKNOWN_COMMAND_STATUS, dispatcher.execute("/unknown", source).status());
    }

    @Test
    @DisplayName("Should execute a command with an argument")
    void testCommandWithArgument() {
        AtomicInteger received = new AtomicInteger(0);
        dispatcher.register(LiteralArgumentBuilder.<TestCommandSource>literal("set").argument("value", new IntegerArgumentType(), arg ->
                arg.executes(c -> {
                    received.set(c.getArgument("value", Integer.class));
                    return CommandResult.SUCCESS;
                })
        ));

        dispatcher.execute("set 123", source);
        assertEquals(123, received.get());
    }
    
    @Test
    @DisplayName("Should execute a command with multiple arguments")
    void testCommandWithMultipleArguments() {
        AtomicBoolean executed = new AtomicBoolean(false);
        dispatcher.register(LiteralArgumentBuilder.<TestCommandSource>literal("tp")
                .argument("x", new IntegerArgumentType(), xArg -> xArg
                .argument("y", new IntegerArgumentType(), yArg -> yArg
                .executes(c -> {
                    int x = c.getArgument("x", Integer.class);
                    int y = c.getArgument("y", Integer.class);
                    assertEquals(10, x);
                    assertEquals(20, y);
                    executed.set(true);
                    return CommandResult.SUCCESS;
                }))));

        dispatcher.execute("tp 10 20", source);
        assertTrue(executed.get());
    }

    @Test
    @DisplayName("Should return incomplete command when not enough arguments are provided")
    void testInvalidUsage_notEnoughArgs() {
        dispatcher.register(LiteralArgumentBuilder.<TestCommandSource>literal("set")
                .argument("value", new IntegerArgumentType(), arg ->
                        arg.executes(c -> CommandResult.SUCCESS))
        );

        CommandResult result = dispatcher.execute("set", source);
        assertEquals(CommandResult.INCOMPLETE_COMMAND, result);
    }

    @Test
    @DisplayName("Should return invalid argument for a wrong argument type")
    void testInvalidUsage_wrongArgumentType() {
        dispatcher.register(LiteralArgumentBuilder.<TestCommandSource>literal("set")
                .argument("value", new IntegerArgumentType(), arg ->
                        arg.executes(c -> CommandResult.SUCCESS))
        );

        CommandResult result = dispatcher.execute("set abc", source);
        assertEquals(CommandResult.INVALID_ARGUMENT, result);
    }
    
    @Test
    @DisplayName("Should execute a subcommand")
    void testSubcommand() {
        AtomicBoolean executed = new AtomicBoolean(false);
        dispatcher.register(LiteralArgumentBuilder.<TestCommandSource>literal("perm")
                .literal("grant", grant ->
                        grant.executes(c -> {
                            executed.set(true);
                            return CommandResult.SUCCESS;
                        })
                ));
        
        dispatcher.execute("perm grant", source);
        assertTrue(executed.get());
    }
}