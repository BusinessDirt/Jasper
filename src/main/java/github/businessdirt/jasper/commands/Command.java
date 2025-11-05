/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

/** Represents a command that can be executed. */
@FunctionalInterface
public interface Command<S extends CommandSource> {

    /**
     * Executes the command.
     *
     * @param context The context in which the command is executed.
     * @return the {@link CommandResult} of the command.
     */
    CommandResult run(CommandContext<S> context);
}
