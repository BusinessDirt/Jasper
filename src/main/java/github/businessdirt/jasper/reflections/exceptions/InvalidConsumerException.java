package github.businessdirt.jasper.reflections.exceptions;

import github.businessdirt.jasper.reflections.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class InvalidConsumerException extends RuntimeException {

    public InvalidConsumerException(
            @NotNull Method method,
            @NotNull Throwable cause
    ) {
        super(String.format("Method %s is not a valid consumer. %s",
                ReflectionUtils.getMethodString(method), getInternalCause(method)), cause);
    }

    private static String getInternalCause(@NotNull Method method) {
        if (method.getParameterCount() != 1) {
            return String.format("Expected parameter count of 1 but was %d.", method.getParameterCount());
        }

        return "Unknown reason.";
    }
}
