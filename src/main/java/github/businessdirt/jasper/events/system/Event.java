package github.businessdirt.jasper.events.system;

import java.util.function.Consumer;

/**
 * The base class for all events.
 * <p>
 * Events are objects that are posted to the {@link EventBus} to signal that something has happened.
 * Listeners can subscribe to event types to be notified when they are posted.
 *
 * @see EventBus
 * @see HandleEvent
 */
public abstract class Event {

    private boolean isCancelled = false;

    /**
     * Checks if the event has been cancelled.
     *
     * @return {@code true} if the event is cancelled, {@code false} otherwise.
     */
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Marks the event as cancelled. This is a private method and should only be called from within the event system.
     * To cancel an event, it must implement the {@link Cancellable} interface and call {@link Cancellable#cancel()}.
     */
    private void setCancelled() {
        this.isCancelled = true;
    }

    /**
     * Posts this event to the default {@link EventBus}.
     *
     * @return {@code true} if the event was cancelled by any of the listeners, {@code false} otherwise.
     */
    public boolean post() {
        return this.post(null);
    }

    /**
     * Posts this event to the default {@link EventBus}.
     *
     * @param onError a {@link Consumer} that will be called if an exception is thrown by a listener.
     * @return {@code true} if the event was cancelled by any of the listeners, {@code false} otherwise.
     */
    public boolean post(Consumer<Throwable> onError) {
        return EventBus.get().getEventHandler(this.getClass()).post(this, onError);
    }

    /**
     * An interface that marks an {@link Event} as cancellable.
     * When an event is cancelled, subsequent listeners with lower priority will not be called
     * unless they are marked with {@link HandleEvent#receiveCancelled()} set to {@code true}.
     */
    public interface Cancellable {
        /**
         * Cancels the event.
         */
        default void cancel() {
            ((Event) this).setCancelled();
        }
    }
}
