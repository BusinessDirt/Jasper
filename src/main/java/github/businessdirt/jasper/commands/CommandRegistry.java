/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import github.businessdirt.jasper.commands.arguments.IntegerArgumentType;
import github.businessdirt.jasper.commands.builder.LiteralArgumentBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import static github.businessdirt.jasper.commands.builder.LiteralArgumentBuilder.literal;

/**
 * Handles client commands. This is a singleton class that holds the command dispatcher and provides methods for registering and handling commands.
 */
@SuppressWarnings("unused")
public class CommandRegistry<S extends CommandSource> {

    private static final Map<Class<? extends CommandSource>, CommandRegistry<? extends CommandSource>> INSTANCES = new HashMap<>();

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
        this.dispatcher.register(root);
    }

    /**
     * Returns the command registry instance for the given command source type.
     *
     * @param type the class of the command source type
     * @param <S> the command source type
     * @return the command registry instance
     */
    public static synchronized <S extends CommandSource> CommandRegistry<S> get(Class<S> type) {
        if (!INSTANCES.containsKey(type)) {
            INSTANCES.put(type, new CommandRegistry<>());
        }

        //noinspection unchecked
        return (CommandRegistry<S>) INSTANCES.get(type);
    }

    /**
     * Handles a command.
     *
     * @param source the command source
     * @param command the command to handle
     * @return the {@code CommandResult} of the executed command
     */
    public CommandResult handle(S source, String command) {
        if (command.startsWith("/")) command = command.substring(1);
        return dispatcher.execute(command, source);
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
