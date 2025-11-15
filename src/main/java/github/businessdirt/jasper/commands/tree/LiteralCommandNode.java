package github.businessdirt.jasper.commands.tree;

import github.businessdirt.jasper.commands.CommandSource;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a command node that is a literal.
 */
public class LiteralCommandNode<S extends CommandSource> extends CommandNode<S> {
    private final String literal;

    /**
     * Constructs a new literal command node.
     *
     * @param literal the literal
     */
    public LiteralCommandNode(@NotNull String literal) {
        this.literal = literal;
    }

    /**
     * Returns the literal of this node.
     *
     * @return the literal of this node
     */
    @Override
    public @NotNull String getName() {
        return literal;
    }
}
