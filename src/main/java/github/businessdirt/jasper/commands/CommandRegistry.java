/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles client commands. This is a singleton class that holds the command dispatcher and provides methods for registering and handling commands.
 */
public class CommandRegistry {

    private static CommandRegistry instance;
    private static final Logger LOGGER = LogManager.getLogger(CommandRegistry.class);

    private final CommandDispatcher dispatcher = new CommandDispatcher();

    private CommandRegistry() {
        this.register("help", root -> root.executes(new HelpCommand(this.dispatcher.getCommands()))
                .argument("page", new IntegerArgumentType(), page ->
                    page.executes(new HelpCommand(this.dispatcher.getCommands()))
                ).build()
        );
    }

    /**
     * Registers a command.
     *
     * @param rootCommand the root command
     * @param builder the command builder
     */
    public void register(String rootCommand, Consumer<LiteralArgumentBuilder> builder) {
        LiteralArgumentBuilder root = literal(rootCommand);
        builder.accept(root);
        this.dispatcher.register(root.build());
    }

    /**
     * Returns the singleton instance of the command handler.
     *
     * @return the singleton instance
     */
    public static synchronized CommandRegistry getInstance() {
        if (instance == null) instance = new CommandRegistry();
        return instance;
    }

    /**
     * Handles a command.
     *
     * @param clientContext the client context
     * @param command the command to handle
     * @return true if the client should continue running, false otherwise
     */
    public boolean handle(ClientContext clientContext, String command) {
        if (command.startsWith("/"))
            command = command.substring(1);

        try {
            return dispatcher.execute(command, clientContext) != -1;
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
        return message.startsWith("/") || message.equalsIgnoreCase("bye");
    }
}
