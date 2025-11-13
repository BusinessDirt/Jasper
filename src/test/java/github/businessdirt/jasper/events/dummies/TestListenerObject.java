package github.businessdirt.jasper.events.dummies;

import github.businessdirt.jasper.events.system.HandleEvent;

public class TestListenerObject {

    public static final TestListenerObject INSTANCE = new TestListenerObject();

    @HandleEvent(priority = HandleEvent.Priority.HIGHEST)
    public void handleDummyEvent(DummyEvent event) {
        System.out.println("eventType=null, event=DummyEvent");
    }

    @HandleEvent(eventType = DummyEvent.class)
    public void handleDummyEvent() {
        System.out.println("eventType=DummyEvent, event=null");
    }

    @HandleEvent(eventType = DummyEvent.class, priority = HandleEvent.Priority.LOWEST)
    public void handleDummyEvent2(DummyEvent event) {
        System.out.println("eventType=DummyEvent, event=DummyEvent");
    }
}
