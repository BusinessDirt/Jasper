/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.arguments.ArgumentType;

/**
 * Represents a command node that is an argument.
 *
 * @param <T> the type of the argument
 */
public class ArgumentCommandNode<T> extends CommandNode {
    private final String name;
    private final ArgumentType<T> type;

    /**
     * Constructs a new argument command node.
     *
     * @param name the name of the argument
     * @param type the type of the argument
     */
    public ArgumentCommandNode(String name, ArgumentType<T> type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the name of the argument.
     *
     * @return the name of the argument
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the argument.
     *
     * @return the type of the argument
     */
    public ArgumentType<T> getType() {
        return type;
    }
}
