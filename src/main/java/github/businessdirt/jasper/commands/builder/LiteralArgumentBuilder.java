/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands.builder;

import github.businessdirt.jasper.commands.CommandSource;
import github.businessdirt.jasper.commands.tree.LiteralCommandNode;
import org.jetbrains.annotations.NotNull;

/**
 * A builder for literal command arguments. A literal argument is a fixed string that must be present in the command.
 */
public class LiteralArgumentBuilder<S extends CommandSource> extends AbstractArgumentBuilder<S, LiteralArgumentBuilder<S>> {

    private LiteralArgumentBuilder(String literal) {
        super(new LiteralCommandNode<>(literal));
    }

    /**
     * Creates a new literal argument builder.
     *
     * @param literal the literal
     * @param <S> the command source type
     * @return the new literal argument builder
     */
    public static <S extends CommandSource> LiteralArgumentBuilder<S> literal(String literal) {
        return new LiteralArgumentBuilder<>(literal);
    }

    /**
     * Builds the literal command node.
     *
     * @return the literal command node
     */
    @Override
    public @NotNull LiteralCommandNode<S> build() {
        return (LiteralCommandNode<S>) this.node;
    }
}
