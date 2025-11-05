/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.arguments.IntegerArgumentType;
import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.builder.LiteralArgumentBuilder;
import bollschweiler.de.lmu.ifi.cip.gitlab2.network.client.ClientContext;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandDispatcherTest {

    private CommandDispatcher dispatcher;
    private ClientContext clientContext; // Can be null for these tests

    @BeforeEach
    void setUp() {
        dispatcher = new CommandDispatcher();
        clientContext = null;
    }

    @Test
    void testSimpleLiteralCommand() throws Exception {
        AtomicBoolean executed = new AtomicBoolean(false);
        dispatcher.register(LiteralArgumentBuilder.literal("test").executes(c -> {
            executed.set(true);
            return 1;
        }).build());

        int result = dispatcher.execute("test", clientContext);
        assertTrue(executed.get());
        assertEquals(1, result);
    }

    @Test
    void testUnknownCommand() {
        assertThrows(Exception.class, () -> dispatcher.execute("unknown", clientContext));
    }

    @Test
    void testCommandWithArgument() throws Exception {
        AtomicInteger received = new AtomicInteger(0);
        dispatcher.register(LiteralArgumentBuilder.literal("set")
                .argument("value", new IntegerArgumentType(), arg -> arg.executes(c -> {
                    received.set(c.getArgument("value", Integer.class));
                    return 1;
                })).build());

        dispatcher.execute("set 123", clientContext);
        assertEquals(123, received.get());
    }
    
    @Test
    void testCommandWithMultipleArguments() throws Exception {
        AtomicBoolean executed = new AtomicBoolean(false);
        dispatcher.register(LiteralArgumentBuilder.literal("tp")
                .argument("x", new IntegerArgumentType(), xArg -> xArg
                .argument("y", new IntegerArgumentType(), yArg -> yArg
                .executes(c -> {
                    int x = c.getArgument("x", Integer.class);
                    int y = c.getArgument("y", Integer.class);
                    assertEquals(10, x);
                    assertEquals(20, y);
                    executed.set(true);
                    return 1;
                }))).build());

        dispatcher.execute("tp 10 20", clientContext);
        assertTrue(executed.get());
    }

    @Test
    void testInvalidUsage_notEnoughArgs() throws Exception {
        dispatcher.register(LiteralArgumentBuilder.literal("set")
                .argument("value", new IntegerArgumentType(), arg -> arg.executes(c -> 1))
                .build());

        int result = dispatcher.execute("set", clientContext);
        assertEquals(0, result); // Should print usage and return 0
    }

    @Test
    void testInvalidUsage_wrongArgumentType() throws Exception {
        dispatcher.register(LiteralArgumentBuilder.literal("set")
                .argument("value", new IntegerArgumentType(), arg -> arg.executes(c -> 1))
                .build());

        int result = dispatcher.execute("set abc", clientContext);
        assertEquals(0, result); // Should print usage and return 0
    }
    
    @Test
    void testSubcommand() throws Exception {
        AtomicBoolean executed = new AtomicBoolean(false);
        dispatcher.register(LiteralArgumentBuilder.literal("perm")
                .literal("grant", grant -> grant.executes(c -> {
                    executed.set(true);
                    return 1;
                })).build());
        
        dispatcher.execute("perm grant", clientContext);
        assertTrue(executed.get());
    }
}
