package github.businessdirt.jasper.events.system.exceptions;

import java.io.Serial;

public class EventBusNotInitializedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EventBusNotInitializedException() {
        super("Use EventBus::initialize(String, Logger) to initialize the EventBus first!");
    }
}
