package github.businessdirt.jasper.events.system.exceptions;

import github.businessdirt.jasper.reflections.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.lang.reflect.Method;

public class ParameterException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ParameterException(
            @NotNull Method method,
            @NotNull String extra
    ) {
        super(String.format("Method %s %s", ReflectionUtils.getMethodString(method), extra));
    }
}
