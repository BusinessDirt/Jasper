/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import github.businessdirt.jasper.commands.tree.ArgumentCommandNode;
import github.businessdirt.jasper.commands.tree.CommandNode;
import github.businessdirt.jasper.commands.tree.LiteralCommandNode;
import github.businessdirt.jasper.commands.tree.RootCommandNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Dispatches commands. It parses the command string and executes the corresponding command.
 * The command structure is represented as a tree of command nodes.
 */
public class CommandDispatcher<S extends CommandSource> {
    private final RootCommandNode<S> root = new RootCommandNode<>();

    /**
     * Registers a command.
     *
     * @param command the command to register
     */
    public void register(LiteralCommandNode<S> command) {
        root.addChild(command);
    }

    /**
     * Returns the registered commands.
     *
     * @return a map of command names to command nodes
     */
    public Map<String, CommandNode<S>> getCommands() {
        return root.getChildren();
    }

    /**
     * Executes a command.
     *
     * @param input the command string to execute
     * @param source the source of the command
     * @return the result of the command execution
     * @throws Exception if an error occurs during command execution
     */
    public int execute(String input, S source) throws Exception {
        StringReader reader = new StringReader(input);
        CommandNode<S> currentNode = root;
        Map<String, Object> arguments = new HashMap<>();
        StringBuilder commandPath = new StringBuilder();

        // Find the initial command node
        String literal = reader.readString();
        CommandNode<S> nextNode = currentNode.getChildren().get(literal);

        if (nextNode == null) throw new Exception("Unknown command");
        currentNode = nextNode;
        commandPath.append(literal);

        // Parse the arguments
        while (reader.canRead()) {
            boolean found = false;
            reader.skip(); // Skip space

            for (CommandNode<S> child : currentNode.getChildren().values()) {
                if (child instanceof ArgumentCommandNode<S, ?> argumentNode) {
                    StringReader fork = new StringReader(reader);
                    try {
                        arguments.put(argumentNode.getName(), argumentNode.getType().parse(fork));
                        currentNode = argumentNode;
                        reader.setCursor(fork.getCursor());
                        found = true;
                        break;
                    } catch (Exception ignored) {
                        // If parsing fails, ignore and try the next child
                    }
                } else if (child instanceof LiteralCommandNode<S> literalNode) {
                    StringReader fork = new StringReader(reader);
                    String nextLiteral = fork.readString();
                    if (literalNode.getName().equals(nextLiteral)) {
                        currentNode = literalNode;
                        reader.setCursor(fork.getCursor());
                        commandPath.append(" ").append(literalNode.getName());
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                System.out.println("Usage: " + currentNode.getUsage(commandPath.toString()));
                return 0;
            }
        }

        if (currentNode.getCommand() == null) {
            System.out.println("Usage: " + currentNode.getUsage(commandPath.toString()));
            return 0;
        }

        return currentNode.getCommand().run(new CommandContext<>(source, arguments));
    }
}
