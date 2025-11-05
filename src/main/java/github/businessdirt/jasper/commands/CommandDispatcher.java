/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree.ArgumentCommandNode;
import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree.CommandNode;
import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree.LiteralCommandNode;
import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.tree.RootCommandNode;
import bollschweiler.de.lmu.ifi.cip.gitlab2.network.client.ClientContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Dispatches commands. It parses the command string and executes the corresponding command.
 * The command structure is represented as a tree of command nodes.
 */
public class CommandDispatcher {
    private final RootCommandNode root = new RootCommandNode();

    /**
     * Registers a command.
     *
     * @param command the command to register
     */
    public void register(LiteralCommandNode command) {
        root.addChild(command);
    }

    public Map<String, CommandNode> getCommands() {
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
    public int execute(String input, ClientContext source) throws Exception {
        StringReader reader = new StringReader(input);
        CommandNode currentNode = root;
        Map<String, Object> arguments = new HashMap<>();
        StringBuilder commandPath = new StringBuilder();

        // Find the initial command node
        String literal = reader.readString();
        CommandNode nextNode = currentNode.getChildren().get(literal);

        if (nextNode == null) throw new Exception("Unknown command");
        currentNode = nextNode;
        commandPath.append(literal);

        // Parse the arguments
        while (reader.canRead()) {
            boolean found = false;
            reader.skip(); // Skip space

            for (CommandNode child : currentNode.getChildren().values()) {
                if (child instanceof ArgumentCommandNode<?> argumentNode) {
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
                } else if (child instanceof LiteralCommandNode literalNode) {
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

        return currentNode.getCommand().run(new CommandContext(source, arguments));
    }
}
