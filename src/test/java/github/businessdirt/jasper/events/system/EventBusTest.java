package github.businessdirt.jasper.events.system;

import github.businessdirt.jasper.events.dummies.DummyCancellableEvent;
import github.businessdirt.jasper.events.dummies.DummyEvent;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class EventBusTest {

    @BeforeAll
    static void init() throws IOException {
        EventBus.initialize("github.businessdirt.jasper", LogManager.getLogger(EventBusTest.class));
    }

    String postEvent(Event event) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output, true));

        @SuppressWarnings("unused")
        boolean cancelled = event.post();

        System.setOut(originalOut);
        return output.toString();
    }

    @Test
    @DisplayName("Should successfully post events to all listeners")
    void testEventPost() {
        String output = postEvent(new DummyEvent());

        assertTrue(output.contains("eventType=null, event=DummyEvent\n"));
        assertTrue(output.contains("eventType=DummyEvent, event=null\n"));
        assertTrue(output.contains("eventType=DummyEvent, event=DummyEvent\n"));
    }

    @Test
    @DisplayName("Should prioritize events correctly")
    void testEventPostPriority() {
        assertEquals("eventType=null, event=DummyEvent\neventType=DummyEvent, event=null\neventType=DummyEvent, event=DummyEvent\n",
                postEvent(new DummyEvent()));
    }

    @Test
    @DisplayName("Should cancel events successfully")
    void testEventPostCancel() {
        String output = postEvent(new DummyCancellableEvent());
        assertTrue(output.contains("handleCancellableEvent->cancel"));
        assertTrue(output.contains("handleCancellableEvent->receiveCancelled"));
        assertFalse(output.contains("UNEXPECTED"));
    }
}