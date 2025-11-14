package github.businessdirt.jasper.events.system.exceptions;

import github.businessdirt.jasper.reflections.ReflectionUtils;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

public class InvalidParameterCountException extends RuntimeException {

    public InvalidParameterCountException(Method method, String extra, Logger logger) {
        super(String.format("Method %s has an invalid parameter count: %s", ReflectionUtils.getMethodString(method), extra));

        if (logger != null) logger.atError().withThrowable(this).log();
    }
}
