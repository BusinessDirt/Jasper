package github.businessdirt.jasper.commands;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the result of a command execution.
 * @param status the status code of the result
 * @param message an optional message that can be used for further debugging or as info
 */
@SuppressWarnings("unused")
public record CommandResult(
        int status,
        @NotNull String message
) {

    public static int QUIT_STATUS = -1;
    public static int SUCCESS_STATUS = 0;
    public static int ERROR_STATUS = 1;
    public static int UNKNOWN_COMMAND_STATUS = 2;
    public static int INCOMPLETE_COMMAND_STATUS = 3;
    public static int INVALID_ARGUMENT_STATUS = 4;

    /**
     * A default {@code CommandResult} that is used when the execution was successful
     */
    public static final CommandResult SUCCESS = new CommandResult(SUCCESS_STATUS, "");

    /**
     * A {@code CommandResult} for an unknown command.
     */
    public static final CommandResult UNKNOWN_COMMAND = new CommandResult(UNKNOWN_COMMAND_STATUS, "Unknown command");

    /**
     * A {@code CommandResult} for an incomplete command.
     */
    public static final CommandResult INCOMPLETE_COMMAND = new CommandResult(INCOMPLETE_COMMAND_STATUS, "Incomplete command");

    /**
     * A {@code CommandResult} for an invalid argument.
     */
    public static final CommandResult INVALID_ARGUMENT = new CommandResult(INVALID_ARGUMENT_STATUS, "Invalid argument");
}
