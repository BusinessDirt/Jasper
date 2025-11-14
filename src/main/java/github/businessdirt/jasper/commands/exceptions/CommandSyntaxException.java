package github.businessdirt.jasper.commands.exceptions;

import java.io.Serial;

public class CommandSyntaxException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CommandSyntaxException(String message) {
        super(message);
    }
}
