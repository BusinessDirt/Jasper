/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands.builder;

import github.businessdirt.jasper.commands.tree.LiteralCommandNode;

/**
 * A builder for literal command arguments. A literal argument is a fixed string that must be present in the command.
 */
public class LiteralArgumentBuilder extends AbstractArgumentBuilder<LiteralArgumentBuilder> {

    private LiteralArgumentBuilder(String literal) {
        super(new LiteralCommandNode(literal));
    }

    /**
     * Creates a new literal argument builder.
     *
     * @param literal the literal
     * @return the new literal argument builder
     */
    public static LiteralArgumentBuilder literal(String literal) {
        return new LiteralArgumentBuilder(literal);
    }

    /**
     * Builds the literal command node.
     *
     * @return the literal command node
     */
    @Override
    public LiteralCommandNode build() {
        return (LiteralCommandNode) node;
    }
}
