package github.businessdirt.jasper.reflections.exceptions;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class ScanningException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ScanningException(
            @NotNull String message,
            @NotNull Throwable cause
    ) {
        super(message, cause);
    }
}
