package github.businessdirt.jasper.events.system.exceptions;

import github.businessdirt.jasper.events.system.Event;
import github.businessdirt.jasper.reflections.ReflectionUtils;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

public class ParameterNotEventException extends RuntimeException {

    public ParameterNotEventException(Method method, Logger logger) {
        super(String.format("Parameter of %s must be a subclass of %s.",
                ReflectionUtils.getMethodString(method), Event.class.getName()));

        if (logger != null) logger.atError().withThrowable(this).log();
    }
}
