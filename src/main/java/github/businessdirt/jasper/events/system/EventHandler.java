package github.businessdirt.jasper.events.system;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class EventHandler {

    private final String name;
    private final List<EventListener> listeners;
    private final boolean canReceiveCancelled;

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

    public String getName() {
        return name;
    }
}
