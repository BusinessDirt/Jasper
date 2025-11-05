/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

/** Represents a command that can be executed. */
@FunctionalInterface
public interface Command {

    /**
     * Executes the command.
     *
     * @param context The context in which the command is executed.
     * @return an integer representing the command's execution status
     */
    int run(CommandContext context);
}
