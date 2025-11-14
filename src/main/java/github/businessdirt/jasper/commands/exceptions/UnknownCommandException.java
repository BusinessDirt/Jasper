package github.businessdirt.jasper.commands.exceptions;

public class UnknownCommandException extends RuntimeException {

    public UnknownCommandException() {
        super("Unknown command");
    }
}
