package github.businessdirt.jasper.events.system.exceptions;

import github.businessdirt.jasper.reflections.ReflectionUtils;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

public class MethodNotPublicException extends RuntimeException {

    public MethodNotPublicException(Method method, Logger logger) {
        super(String.format("Method %s is not public", ReflectionUtils.getMethodString(method)));

        if (logger != null) logger.atError().withThrowable(this).log();
    }
}
