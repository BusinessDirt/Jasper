package github.businessdirt.jasper.reflections.exceptions;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;

public class ScanningException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ScanningException(
            @NotNull String message,
            @NotNull Throwable cause,
            @Nullable Logger logger
    ) {
        super(message, cause);
        if (logger != null) logger.atError().withThrowable(cause).log(message);
    }
}
