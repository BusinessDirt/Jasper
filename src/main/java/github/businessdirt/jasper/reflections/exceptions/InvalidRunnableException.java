package github.businessdirt.jasper.reflections.exceptions;

import github.businessdirt.jasper.reflections.ReflectionUtils;

import java.lang.reflect.Method;

public class InvalidRunnableException extends RuntimeException {

    public InvalidRunnableException(Method method, Throwable cause) {
        super(String.format("Method %s is not a valid runnable", ReflectionUtils.getMethodString(method)), cause);
    }
}
