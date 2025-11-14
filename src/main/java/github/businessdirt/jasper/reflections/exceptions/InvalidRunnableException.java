package github.businessdirt.jasper.reflections.exceptions;

import github.businessdirt.jasper.reflections.ReflectionUtils;

import java.lang.reflect.Method;

public class InvalidRunnableException extends RuntimeException {

    public InvalidRunnableException(Method method, Throwable cause) {
        super(String.format("Method %s is not a valid runnable: %s",
                ReflectionUtils.getMethodString(method), getInternalCause(method)), cause);
    }

    private static String getInternalCause(Method method) {
        if (method.getParameterCount() != 0) {
            return String.format("Expected parameter count of 0 but was %d.", method.getParameterCount());
        }

        return "Unknown reason.";
    }
}
