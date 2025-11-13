package github.businessdirt.jasper.events.system;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public abstract class Event {

    private boolean isCancelled = false;

    public boolean isCancelled() {
        return isCancelled;
    }

    private void setCancelled() {
        this.isCancelled = true;
    }

    public boolean post() {
        return this.post(null);
    }

    public boolean post(Consumer<Throwable> onError) {
        return EventBus.get().getEventHandler(this.getClass()).post(this, onError);
    }

    public interface Cancellable {
        default void cancel() {
            ((Event) this).setCancelled();
        }
    }
}
