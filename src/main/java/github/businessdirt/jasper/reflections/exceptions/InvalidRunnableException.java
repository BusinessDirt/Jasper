package github.businessdirt.jasper.reflections.exceptions;

import github.businessdirt.jasper.reflections.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.lang.reflect.Method;

public class InvalidRunnableException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidRunnableException(
            @NotNull Method method,
            @NotNull Throwable cause
    ) {
        super(String.format("Method %s is not a valid runnable: %s",
                ReflectionUtils.getMethodString(method), getInternalCause(method)), cause);
    }

    private static String getInternalCause(@NotNull Method method) {
        if (method.getParameterCount() != 0) {
            return String.format("Expected parameter count of 0 but was %d.", method.getParameterCount());
        }

        return "Unknown reason.";
    }
}
