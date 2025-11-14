package github.businessdirt.jasper.reflections.exceptions;

import org.apache.logging.log4j.Logger;

public class ScanningException extends RuntimeException {

    public ScanningException(String message, Throwable cause, Logger logger) {
        super(message, cause);
        if (logger != null) logger.atError().withThrowable(cause).log(message);
    }
}
