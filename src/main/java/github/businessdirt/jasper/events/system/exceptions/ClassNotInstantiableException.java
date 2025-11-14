package github.businessdirt.jasper.events.system.exceptions;

import org.apache.logging.log4j.Logger;

public class ClassNotInstantiableException extends RuntimeException {

    public ClassNotInstantiableException(Class<?> cls, Logger logger) {
        super(String.format("No default constructor or INSTANCE field found for %s", cls.getName()));

        if (logger != null) logger.atError().withThrowable(this).log();
    }
}
