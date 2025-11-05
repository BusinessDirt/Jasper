/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import github.businessdirt.jasper.commands.arguments.IntegerArgumentType;
import github.businessdirt.jasper.commands.builder.LiteralArgumentBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static github.businessdirt.jasper.commands.builder.LiteralArgumentBuilder.literal;

/**
 * Handles client commands. This is a singleton class that holds the command dispatcher and provides methods for registering and handling commands.
 */
@SuppressWarnings("unused")
public class CommandRegistry<S extends CommandSource> {

    private static final Map<Class<? extends CommandSource>, CommandRegistry<CommandSource>> INSTANCES = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger(CommandRegistry.class);

    private final CommandDispatcher<S> dispatcher = new CommandDispatcher<>();

    private CommandRegistry() {
        this.register("help", root -> root.executes(new HelpCommand<>(this.dispatcher.getCommands()))
                .argument("page", new IntegerArgumentType(), page ->
                    page.executes(new HelpCommand<>(this.dispatcher.getCommands()))
                ).build()
        );
    }

    /**
     * Registers a command.
     *
     * @param rootCommand the root command
     * @param builder the command builder
     */
    public void register(String rootCommand, Consumer<LiteralArgumentBuilder<S>> builder) {
        LiteralArgumentBuilder<S> root = literal(rootCommand);
        builder.accept(root);
        this.dispatcher.register(root.build());
    }

    /**
     * Returns the command registry instance for the given command source type.
     *
     * @param clazz the command source class
     * @return the command registry instance
     */
    public static synchronized CommandRegistry<? extends CommandSource> get(Class<? extends CommandSource> clazz) {
        return INSTANCES.putIfAbsent(clazz, new CommandRegistry<>());
    }

    /**
     * Handles a command.
     *
     * @param source the command source
     * @param command the command to handle
     * @return true if the client should continue running, false otherwise
     */
    public boolean handle(S source, String command) {
        if (command.startsWith("/"))
            command = command.substring(1);

        try {
            return dispatcher.execute(command, source) != -1;
        } catch (Exception e) {
            LOGGER.error(e);
            return true;
        }
    }

    /**
     * Checks if a message is a command.
     *
     * @param message the message to check
     * @return true if the message is a command, false otherwise
     */
    public static boolean isMessageCommand(String message) {
        return message.startsWith("/");
    }
}
