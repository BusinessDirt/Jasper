package github.businessdirt.jasper.commands;

import github.businessdirt.jasper.commands.arguments.IntegerArgumentType;
import github.businessdirt.jasper.commands.builder.LiteralArgumentBuilder;
import github.businessdirt.jasper.commands.exceptions.CommandSyntaxException;
import github.businessdirt.jasper.commands.exceptions.UnknownCommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

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
        dispatcher.register(LiteralArgumentBuilder.<TestCommandSource>literal("test").executes(_ -> {
            executed.set(true);
            return CommandResult.SUCCESS_STATUS;
        }));

        int result = dispatcher.execute("test", source);
        assertTrue(executed.get());
        assertEquals(CommandResult.SUCCESS_STATUS, result);
    }

    @Test
    @DisplayName("Should throw exception for an unknown command")
    void testUnknownCommand() {
        assertThrows(UnknownCommandException.class, () -> dispatcher.execute("/unknown", source));
    }

    @Test
    @DisplayName("Should execute a command with an argument")
    void testCommandWithArgument() {
        AtomicInteger received = new AtomicInteger(0);
        dispatcher.register(LiteralArgumentBuilder.<TestCommandSource>literal("set").argument("value", new IntegerArgumentType(), arg ->
                arg.executes(c -> {
                    received.set(c.getArgument("value", Integer.class));
                    return CommandResult.SUCCESS_STATUS;
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
                    return CommandResult.SUCCESS_STATUS;
                }))));

        dispatcher.execute("tp 10 20", source);
        assertTrue(executed.get());
    }

    @Test
    @DisplayName("Should throw exception when not enough arguments are provided")
    void testInvalidUsage_notEnoughArgs() {
        dispatcher.register(LiteralArgumentBuilder.<TestCommandSource>literal("set")
                .argument("value", new IntegerArgumentType(), arg ->
                        arg.executes(_ -> CommandResult.SUCCESS_STATUS))
        );

        assertThrows(CommandSyntaxException.class, () -> dispatcher.execute("set", source));
    }

    @Test
    @DisplayName("Should return invalid argument for a wrong argument type")
    void testInvalidUsage_wrongArgumentType() {
        dispatcher.register(LiteralArgumentBuilder.<TestCommandSource>literal("set")
                .argument("value", new IntegerArgumentType(), arg ->
                        arg.executes(_ -> CommandResult.SUCCESS_STATUS))
        );

        assertThrows(CommandSyntaxException.class, () -> dispatcher.execute("set abc", source));
    }
    
    @Test
    @DisplayName("Should execute a subcommand")
    void testSubcommand() {
        AtomicBoolean executed = new AtomicBoolean(false);
        dispatcher.register(LiteralArgumentBuilder.<TestCommandSource>literal("perm")
                .literal("grant", grant ->
                        grant.executes(_ -> {
                            executed.set(true);
                            return CommandResult.SUCCESS_STATUS;
                        })
                ));
        
        dispatcher.execute("perm grant", source);
        assertTrue(executed.get());
    }
}