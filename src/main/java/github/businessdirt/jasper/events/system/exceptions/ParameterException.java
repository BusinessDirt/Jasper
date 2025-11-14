package github.businessdirt.jasper.events.system.exceptions;

import github.businessdirt.jasper.reflections.ReflectionUtils;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public class ParameterException extends RuntimeException {

    public ParameterException(
            @NotNull Method method,
            @NotNull String extra,
            @Nullable Logger logger
    ) {
        super(String.format("Method %s %s", ReflectionUtils.getMethodString(method), extra));

        if (logger != null) logger.atError().withThrowable(this).log();
    }
}
