/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.Command;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a node in the command tree.
 */
public abstract class CommandNode {
    private final Map<String, CommandNode> children = new LinkedHashMap<>();
    private Command command;

    /**
     * Adds a child to this node.
     *
     * @param node the child to add
     */
    public void addChild(CommandNode node) {
        children.put(node.getName(), node);
    }

    /**
     * Sets the command to be executed when this node is reached.
     *
     * @param command the command to execute
     */
    public void executes(Command command) {
        this.command = command;
    }

    /**
     * Returns the children of this node.
     *
     * @return the children of this node
     */
    public Map<String, CommandNode> getChildren() {
        return children;
    }

    /**
     * Returns the command to be executed when this node is reached.
     *
     * @return the command to be executed
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Returns the name of this node.
     *
     * @return the name of this node
     */
    public abstract String getName();

    /**
     * Returns the usage of this command.
     *
     * @return the usage of this command
     */
    public String getUsage() {
        return getUsage(getName());
    }

    private String getUsageFragment() {
        if (this instanceof ArgumentCommandNode) {
            return "<" + getName() + ">";
        } else {
            return getName();
        }
    }

    /**
     * Returns the usage of this command.
     *
     * @param base the base of the command
     * @return the usage of this command
     */
    public String getUsage(String base) {
        StringBuilder usage = new StringBuilder(base.equals("bye") ? "" : "/");
        usage.append(base);

        CommandNode currentNode = this;
        while (!currentNode.getChildren().isEmpty()) {
            if (currentNode.getChildren().size() > 1) {
                usage.append(" [");
                boolean first = true;
                for (CommandNode child : currentNode.getChildren().values()) {
                    if (!first) {
                        usage.append("|");
                    }
                    usage.append(child.getUsageFragment());
                    first = false;
                }
                usage.append("]");
                break; // End after showing choices
            }

            CommandNode child = currentNode.getChildren().values().iterator().next();
            usage.append(" ").append(child.getUsageFragment());
            currentNode = child;
        }

        return usage.toString();
    }
}
