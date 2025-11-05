/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import github.businessdirt.jasper.commands.tree.CommandNode;

import java.util.Map;

/**
 * A command that displays a help message with a list of available commands.
 *
 * @param commands the available commands
 */
public record HelpCommand<S extends CommandSource>(Map<String, CommandNode<S>> commands) implements Command<S> {

    private static final int COMMANDS_PER_PAGE = 5;

    /**
     * Executes the command.
     *
     * @param context the command context
     * @return the {@link CommandResult}
     */
    @Override
    public CommandResult run(CommandContext<S> context) {
        Integer page = context.getArgument("page", Integer.class);
        if (page == null || page < 1) page = 1;

        int totalCommands = commands.size();
        int totalPages = (int) Math.ceil((double) totalCommands / COMMANDS_PER_PAGE);

        if (page > totalPages) page = totalPages;

        /*
        TextBoxBuilder boxBuilder = new TextBoxBuilder("Showing help page %d of %d (/help <page>)", page, totalPages);

        int index = 0;
        int startIndex = (page - 1) * COMMANDS_PER_PAGE;
        int endIndex = Math.min(startIndex + COMMANDS_PER_PAGE, totalCommands);
        for (CommandNode<S> command : commands.values()) {
            if (index >= startIndex && index < endIndex)
                boxBuilder.literal(command.getUsage());
            index++;
        }
        System.out.println(boxBuilder.build());

        */

        return CommandResult.SUCCESS;
    }
}
