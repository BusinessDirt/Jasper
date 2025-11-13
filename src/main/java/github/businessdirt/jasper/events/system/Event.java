package github.businessdirt.jasper.events.system;

import java.util.function.Consumer;

public abstract class Event {

    private boolean isCancelled = false;

    public boolean isCancelled() {
        return isCancelled;
    }

    private void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public boolean post() {
        return this.post(null);
    }

    public boolean post(Consumer<Throwable> onError) {
        return EventBus.getEventHandler(this.getClass()).post(this, onError);
    }

    public interface Cancellable {
        default void cancel() {
            ((Event) this).setCancelled(true);
        }
    }
}
