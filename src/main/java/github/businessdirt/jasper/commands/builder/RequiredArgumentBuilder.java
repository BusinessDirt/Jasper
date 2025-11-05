/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands.builder;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.arguments.ArgumentType;
import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree.ArgumentCommandNode;

/**
 * A builder for required command arguments.
 *
 * @param <T> the type of the argument
 */
public class RequiredArgumentBuilder<T> extends AbstractArgumentBuilder<RequiredArgumentBuilder<T>> {

    private RequiredArgumentBuilder(String name, ArgumentType<T> type) {
        super(new ArgumentCommandNode<>(name, type));
    }

    /**
     * Creates a new required argument builder.
     *
     * @param name the name of the argument
     * @param type the type of the argument
     * @param <T> the type of the argument
     * @return the new required argument builder
     */
    public static <T> RequiredArgumentBuilder<T> argument(String name, ArgumentType<T> type) {
        return new RequiredArgumentBuilder<>(name, type);
    }

    /**
     * Builds the argument command node.
     *
     * @return the argument command node
     */
    @Override
    public ArgumentCommandNode<T> build() {
        return (ArgumentCommandNode<T>) node;
    }
}
