package github.businessdirt.jasper.reflections.exceptions;

import github.businessdirt.jasper.reflections.ReflectionUtils;

import java.lang.reflect.Method;

public class InvalidConsumerException extends RuntimeException {

    public InvalidConsumerException(Method method, Throwable cause) {
        super(String.format("Method %s is not a valid consumer", ReflectionUtils.getMethodString(method)), cause);
    }
}
