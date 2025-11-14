package github.businessdirt.jasper.reflections.exceptions;

import github.businessdirt.jasper.reflections.ReflectionUtils;

import java.lang.reflect.Method;

public class InvalidConsumerException extends RuntimeException {

    public InvalidConsumerException(Method method, Throwable cause) {
        super(String.format("Method %s is not a valid consumer. %s",
                ReflectionUtils.getMethodString(method), getInternalCause(method)), cause);
    }

    private static String getInternalCause(Method method) {
        if (method.getParameterCount() != 1) {
            return String.format("Expected parameter count of 1 but was %d.", method.getParameterCount());
        }

        return "Unknown reason.";
    }
}
