package github.businessdirt.jasper.events.system.exceptions;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClassNotInstantiableException extends RuntimeException {

    public ClassNotInstantiableException(
            @NotNull Class<?> cls,
            @Nullable Logger logger
    ) {
        super(String.format("No default constructor or INSTANCE field found for %s", cls.getName()));

        if (logger != null) logger.atError().withThrowable(this).log();
    }
}
