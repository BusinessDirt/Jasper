/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands.tree;

import github.businessdirt.jasper.commands.CommandSource;

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
    public LiteralCommandNode(String literal) {
        this.literal = literal;
    }

    /**
     * Returns the literal of this node.
     *
     * @return the literal of this node
     */
    @Override
    public String getName() {
        return literal;
    }
}
