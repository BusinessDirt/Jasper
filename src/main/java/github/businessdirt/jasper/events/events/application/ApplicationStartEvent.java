package github.businessdirt.jasper.events.events.application;

import github.businessdirt.jasper.events.system.Event;

public class ApplicationStartEvent extends Event {

    public ApplicationStartEvent() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> new ApplicationShutdownEvent().post()));
    }
}
