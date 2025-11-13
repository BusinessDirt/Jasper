package github.businessdirt.jasper.events.system;

import github.businessdirt.jasper.events.dummies.DummyEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventBusTest {

    @BeforeAll
    static void init() {
        EventBus.initialize("github.businessdirt.jasper");
    }

    String postEvent() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output, true));

        new DummyEvent().post();

        System.setOut(originalOut);
        return output.toString();
    }

    @Test
    @DisplayName("Should successfully post events to all listeners")
    void testEventPost() {
        String output = postEvent();

        assertTrue(output.contains("eventType=null, event=DummyEvent\n"));
        assertTrue(output.contains("eventType=DummyEvent, event=null\n"));
        assertTrue(output.contains("eventType=DummyEvent, event=DummyEvent\n"));
    }

    @Test
    @DisplayName("Should prioritize events correctly")
    void testEventPostPriority() {
        assertEquals("eventType=null, event=DummyEvent\neventType=DummyEvent, event=null\neventType=DummyEvent, event=DummyEvent\n",
                postEvent());
    }
}