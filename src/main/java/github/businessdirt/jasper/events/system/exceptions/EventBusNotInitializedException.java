package github.businessdirt.jasper.events.system.exceptions;

import org.apache.logging.log4j.Logger;

public class EventBusNotInitializedException extends RuntimeException {

    public EventBusNotInitializedException() {
        super("Use EventBus::initialize(String, Logger) to initialize the EventBus first!");
    }
}
