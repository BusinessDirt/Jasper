package github.businessdirt.jasper.events.events;

import github.businessdirt.jasper.commands.CommandDispatcher;
import github.businessdirt.jasper.commands.CommandSource;
import github.businessdirt.jasper.commands.HelpCommand;
import github.businessdirt.jasper.commands.arguments.IntegerArgumentType;
import github.businessdirt.jasper.commands.builder.LiteralArgumentBuilder;
import github.businessdirt.jasper.events.system.Event;

import java.util.function.Consumer;

import static github.businessdirt.jasper.commands.builder.LiteralArgumentBuilder.literal;

public class CommandRegistrationEvent<S extends CommandSource> extends Event {

    private final CommandDispatcher<S> dispatcher;

    public CommandRegistrationEvent(CommandDispatcher<S> dispatcher) {
        this.dispatcher = dispatcher;

        this.register("help", root ->
                root.executes(new HelpCommand<>(this.dispatcher.getCommands()))
                        .argument("page", new IntegerArgumentType(), page ->
                                page.executes(new HelpCommand<>(this.dispatcher.getCommands())))
                        .build());
    }

    /**
     * Registers a command.
     *
     * @param rootCommand the root command
     * @param builder the command builder
     */
    public void register(
            String rootCommand,
            Consumer<LiteralArgumentBuilder<S>> builder
    ) {
        LiteralArgumentBuilder<S> root = literal(rootCommand);
        builder.accept(root);
        this.dispatcher.register(root);
    }
}
