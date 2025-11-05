/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands.builder;

import github.businessdirt.jasper.commands.Command;
import github.businessdirt.jasper.commands.CommandSource;
import github.businessdirt.jasper.commands.arguments.ArgumentType;
import github.businessdirt.jasper.commands.tree.CommandNode;

import java.util.function.Consumer;

/**
 * An abstract builder for command arguments.
 *
 * @param <S> the type of the command source
 * @param <B> the type of the builder
 */
@SuppressWarnings("unused")
public abstract class AbstractArgumentBuilder<S extends CommandSource, B extends AbstractArgumentBuilder<S, B>> {
    protected final CommandNode<S> node;

    /**
     * Constructs a new abstract argument builder.
     *
     * @param node the command node
     */
    protected AbstractArgumentBuilder(CommandNode<S> node) {
        this.node = node;
    }

    @SuppressWarnings("unchecked")
    private B then(CommandNode<S> child) {
        this.node.addChild(child);
        return (B) this;
    }

    /**
     * Adds a literal argument to the command.
     *
     * @param literal the literal argument
     * @param consumer the consumer for the literal argument builder
     * @return the builder
     */
    public B literal(String literal, Consumer<LiteralArgumentBuilder<S>> consumer) {
        LiteralArgumentBuilder<S> literalBuilder = LiteralArgumentBuilder.literal(literal);
        consumer.accept(literalBuilder);
        return this.then(literalBuilder.build());
    }

    /**
     * Adds a required argument to the command.
     *
     * @param argName the name of the argument
     * @param type the type of the argument
     * @param consumer the consumer for the required argument builder
     * @param <T> the type of the argument
     * @return the builder
     */
    public <T> B argument(String argName, ArgumentType<T> type, Consumer<RequiredArgumentBuilder<S, T>> consumer) {
        RequiredArgumentBuilder<S, T> argumentBuilder = RequiredArgumentBuilder.argument(argName, type);
        consumer.accept(argumentBuilder);
        return this.then(argumentBuilder.build());
    }

    /**
     * Sets the command to be executed.
     *
     * @param command the command to be executed
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    public B executes(Command<S> command) {
        this.node.executes(command);
        return (B) this;
    }

    /**
     * Builds the command node.
     *
     * @return the command node
     */
    public abstract CommandNode<S> build();
}
