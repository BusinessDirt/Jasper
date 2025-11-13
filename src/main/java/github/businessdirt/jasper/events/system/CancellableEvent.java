package github.businessdirt.jasper.events.system;

/**
 * An abstract implementation of a cancellable event.
 * Events that can be cancelled should extend this class.
 *
 * @see Event
 * @see Event.Cancellable
 */
public abstract class CancellableEvent extends Event implements Event.Cancellable {
}
