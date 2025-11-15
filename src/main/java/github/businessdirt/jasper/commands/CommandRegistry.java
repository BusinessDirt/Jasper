package github.businessdirt.jasper.commands;

import github.businessdirt.jasper.events.events.CommandRegistrationEvent;
import github.businessdirt.jasper.reflections.Reflections;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles client commands. This is a singleton class that holds the command dispatcher and provides methods for registering and handling commands.
 */
public class CommandRegistry {

    private static final Map<Class<? extends CommandSource>, CommandDispatcher<? extends CommandSource>> INSTANCES = new HashMap<>();

    /**
     * Checks if a message is a command.
     *
     * @param message the message to check
     * @return true if the message is a command, false otherwise
     */
    public static boolean isMessageCommand(@NotNull String message) {
        return message.startsWith("/");
    }

    public static void initialize(
            @NotNull String basePackage
    ) throws IOException {
        Reflections reflections = new Reflections(basePackage);
        reflections.getSubTypesOf(CommandSource.class).forEach(sourceClass ->
                new CommandRegistrationEvent<>(CommandRegistry.get(sourceClass)).post());
    }

    /**
     * Returns the CommandDispatcher owner for the given command source type.
     *
     * @param type the class of the command source type
     * @param <S> the command source type
     * @return the command registry owner
     */
    @SuppressWarnings("unchecked")
    public static synchronized <S extends CommandSource> CommandDispatcher<S> get(
            @NotNull Class<S> type
    ) {
        if (!INSTANCES.containsKey(type)) {
            INSTANCES.put(type, new CommandDispatcher<S>());
        }

        return (CommandDispatcher<S>) INSTANCES.get(type);
    }

    /**
     * Handles a command.
     *
     * @param type the class of the command source
     * @param source the command source
     * @param command the command to handle
     * @param <S> the command source type
     * @return the {@code CommandResult} of the executed command
     */
    public static synchronized <S extends CommandSource> int handle(
            @NotNull Class<S> type,
            @NotNull S source,
            @NotNull String command
    ) {
        if (command.startsWith("/")) command = command.substring(1);
        return get(type).execute(command, source);
    }
}
