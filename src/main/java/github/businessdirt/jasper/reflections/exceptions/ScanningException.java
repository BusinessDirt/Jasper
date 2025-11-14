package github.businessdirt.jasper.reflections.exceptions;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScanningException extends RuntimeException {

    public ScanningException(
            @NotNull String message,
            @NotNull Throwable cause,
            @Nullable Logger logger
    ) {
        super(message, cause);
        if (logger != null) logger.atError().withThrowable(cause).log(message);
    }
}
