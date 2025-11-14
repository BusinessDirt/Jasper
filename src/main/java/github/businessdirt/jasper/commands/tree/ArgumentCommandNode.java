/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands.tree;

import github.businessdirt.jasper.commands.CommandSource;
import github.businessdirt.jasper.commands.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a command node that is an argument.
 *
 * @param <T> the type of the argument
 */
public class ArgumentCommandNode<S extends CommandSource, T> extends CommandNode<S> {
    private final String name;
    private final ArgumentType<T> type;

    /**
     * Constructs a new argument command node.
     *
     * @param name the name of the argument
     * @param type the type of the argument
     */
    public ArgumentCommandNode(
            @NotNull String name,
            @NotNull ArgumentType<T> type
    ) {
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the name of the argument.
     *
     * @return the name of the argument
     */
    @Override
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Returns the type of the argument.
     *
     * @return the type of the argument
     */
    public @NotNull ArgumentType<T> getType() {
        return this.type;
    }
}
