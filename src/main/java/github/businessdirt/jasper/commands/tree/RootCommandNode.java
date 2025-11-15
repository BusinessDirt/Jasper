package github.businessdirt.jasper.commands.tree;

import github.businessdirt.jasper.commands.CommandSource;
import org.jetbrains.annotations.NotNull;

/** Represents the root node of the command tree. */
public class RootCommandNode<S extends CommandSource> extends CommandNode<S> {

    /**
     * Returns the name of this node.
     *
     * @return the name of this node
     */
    @Override
    public @NotNull String getName() {
        return "";
    }
}
