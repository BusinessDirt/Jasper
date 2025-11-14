package github.businessdirt.jasper.events.system;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Handles the posting of a specific event type to its listeners.
 * It manages a sorted list of listeners and invokes them in order of priority.
 */
public class EventHandler {

    private final String name;
    private final List<EventListener> listeners;
    private final boolean canReceiveCancelled;

    /**
     * Constructs a new {@link EventHandler}.
     *
     * @param event     the event class this handler is for.
     * @param listeners the list of listeners for this event.
     */
    public EventHandler(Class<? extends Event> event, List<EventListener> listeners) {
        String eventName = event.getName();
        String[] parts = eventName.split("\\.");
        String lastPart = parts.length > 0 ? parts[parts.length - 1] : eventName;
        this.name = lastPart.replace("$", ".");

        List<EventListener> sortedListeners = new ArrayList<>(listeners);
        sortedListeners.sort(Comparator.comparingInt(listener -> listener.priority().asInt()));
        this.listeners = sortedListeners;

        boolean anyCanReceiveCancelled = false;
        for (EventListener listener : listeners) {
            if (listener.canReceiveCancelled()) {
                anyCanReceiveCancelled = true;
                break;
            }
        }

        this.canReceiveCancelled = anyCanReceiveCancelled;
    }

    /**
     * Posts an event to all its listeners.
     *
     * @param event   the event to post.
     * @param onError a {@link Consumer} that will be called if an exception is thrown by a listener.
     * @return {@code true} if the event was cancelled by any of the listeners, {@code false} otherwise.
     */
    public boolean post(Event event, Consumer<Throwable> onError) {
        if (this.listeners.isEmpty()) return false;

        for (EventListener listener : this.listeners) {
            if (!listener.shouldInvoke(event)) continue;

            try {
                listener.invoker().accept(event);
            } catch (Throwable throwable) {
                if (onError != null) onError.accept(throwable);
            }

            if (event.isCancelled() && !this.canReceiveCancelled) break;
        }

        return event.isCancelled();
    }

    /**
     * @return the simple name of the event this handler is for.
     */
    public String getName() {
        return name;
    }
}
