package github.businessdirt.jasper.events.dummies;

import github.businessdirt.jasper.events.system.HandleEvent;

public class TestCancellableListener {

    @HandleEvent(priority = HandleEvent.Priority.HIGHEST)
    public void handleEventHighest(DummyCancellableEvent event) {
        System.out.println("handleCancellableEvent->cancel");
        event.cancel();
    }

    @HandleEvent(priority = HandleEvent.Priority.MEDIUM)
    public void handleEventMedium(DummyCancellableEvent event) {
        System.out.println("UNEXPECTED");
    }

    @HandleEvent(priority = HandleEvent.Priority.LOWEST, receiveCancelled = true)
    public void handleEventLowest(DummyCancellableEvent event) {
        System.out.println("handleCancellableEvent->receiveCancelled");
    }
}
