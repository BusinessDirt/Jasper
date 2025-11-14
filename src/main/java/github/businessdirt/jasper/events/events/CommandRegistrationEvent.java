package github.businessdirt.jasper.events.events;

import github.businessdirt.jasper.commands.CommandDispatcher;
import github.businessdirt.jasper.commands.CommandSource;
import github.businessdirt.jasper.commands.HelpCommand;
import github.businessdirt.jasper.commands.arguments.IntegerArgumentType;
import github.businessdirt.jasper.commands.builder.LiteralArgumentBuilder;
import github.businessdirt.jasper.events.system.Event;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static github.businessdirt.jasper.commands.builder.LiteralArgumentBuilder.literal;

@SuppressWarnings("unused")
public class CommandRegistrationEvent<S extends CommandSource> extends Event {

    private final CommandDispatcher<S> dispatcher;

    public CommandRegistrationEvent(@NotNull CommandDispatcher<S> dispatcher) {
        this.dispatcher = dispatcher;

        LiteralArgumentBuilder<S> root = literal("help");
        root.executes(new HelpCommand<>(dispatcher.getCommands()))
                .argument("page", new IntegerArgumentType(), page ->
                        page.executes(new HelpCommand<>(dispatcher.getCommands())))
                .build();

        this.dispatcher.register(root);


    }

    /**
     * Registers a command.
     *
     * @param rootCommand the root command
     * @param builder the command builder
     */
    public void register(
            @NotNull String rootCommand,
            @NotNull Consumer<LiteralArgumentBuilder<S>> builder
    ) {
        LiteralArgumentBuilder<S> root = literal(rootCommand);
        builder.accept(root);
        this.dispatcher.register(root);
    }
}
