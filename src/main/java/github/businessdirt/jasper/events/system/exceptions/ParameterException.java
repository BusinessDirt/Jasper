package github.businessdirt.jasper.events.system.exceptions;

import github.businessdirt.jasper.reflections.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class ParameterException extends RuntimeException {

    public ParameterException(
            @NotNull Method method,
            @NotNull String extra
    ) {
        super(String.format("Method %s %s", ReflectionUtils.getMethodString(method), extra));
    }
}
