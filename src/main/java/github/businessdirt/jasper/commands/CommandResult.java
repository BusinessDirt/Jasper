package github.businessdirt.jasper.commands;

public record CommandResult(int status, String message) {

    public static int SUCCESS_STATUS = 0;
    public static int ERROR_STATUS = 1;

    public static final CommandResult SUCCESS = new CommandResult(SUCCESS_STATUS, null);
}
