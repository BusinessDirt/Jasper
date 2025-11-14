package github.businessdirt.jasper.commands.exceptions;

import java.io.Serial;

public class UnknownCommandException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnknownCommandException() {
        super("Unknown command");
    }
}
