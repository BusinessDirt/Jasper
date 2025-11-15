
package github.businessdirt.jasper.commands;

import org.jetbrains.annotations.NotNull;

/** Represents a command that can be executed. */
@FunctionalInterface
public interface Command<S extends CommandSource> {

    /**
     * Executes the command.
     *
     * @param context The context in which the command is executed.
     * @return the {@link CommandResult} of the command.
     */
    int run(@NotNull CommandContext<S> context);
}
