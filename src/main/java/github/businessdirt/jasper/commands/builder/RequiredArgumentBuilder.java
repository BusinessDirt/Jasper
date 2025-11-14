/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands.builder;

import github.businessdirt.jasper.commands.CommandSource;
import github.businessdirt.jasper.commands.arguments.ArgumentType;
import github.businessdirt.jasper.commands.tree.ArgumentCommandNode;
import org.jetbrains.annotations.NotNull;

/**
 * A builder for required command arguments.
 *
 * @param <T> the type of the argument
 */
public class RequiredArgumentBuilder<S extends CommandSource, T> extends AbstractArgumentBuilder<S, RequiredArgumentBuilder<S, T>> {

    private RequiredArgumentBuilder(String name, ArgumentType<T> type) {
        super(new ArgumentCommandNode<>(name, type));
    }

    /**
     * Creates a new required argument builder.
     *
     * @param name the name of the argument
     * @param type the type of the argument
     * @param <S> the command source type
     * @param <T> the type of the argument
     * @return the new required argument builder
     */
    public static <S extends CommandSource, T> RequiredArgumentBuilder<S, T> argument(String name, ArgumentType<T> type) {
        return new RequiredArgumentBuilder<>(name, type);
    }

    /**
     * Builds the argument command node.
     *
     * @return the argument command node
     */
    @Override
    @SuppressWarnings("unchecked") // needed for Xlint:all
    public @NotNull ArgumentCommandNode<S, T> build() {
        return (ArgumentCommandNode<S, T>) node;
    }
}
