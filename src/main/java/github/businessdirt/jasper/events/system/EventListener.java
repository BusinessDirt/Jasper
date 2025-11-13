package github.businessdirt.jasper.events.system;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A record that represents a listener for a specific event.
 * It contains the listener's name, invoker, priority, and other options.
 *
 * @param name                the name of the listener method.
 * @param invoker             a {@link Consumer} that invokes the listener method.
 * @param priority            the priority of the listener.
 * @param canReceiveCancelled whether the listener can receive cancelled events.
 * @param predicates          a list of predicates to test before invoking the listener.
 */
public record EventListener(
    String name,
    Consumer<Object> invoker,
    HandleEvent.Priority priority,
    boolean canReceiveCancelled,
    List<Predicate<Event>> predicates
) {

    /**
     * Creates a new {@link EventListener} from the given parameters.
     *
     * @param name    the name of the listener method.
     * @param invoker a {@link Consumer} that invokes the listener method.
     * @param options the {@link HandleEvent} annotation of the listener method.
     * @return a new {@link EventListener} instance.
     */
    public static EventListener of(
            String name,
            Consumer<Object> invoker,
            HandleEvent options
    ) {
        List<Predicate<Event>> predicates = new ArrayList<>();
        if (!options.receiveCancelled()) predicates.add(event -> !event.isCancelled());
        return new EventListener(name, invoker, options.priority(), options.receiveCancelled(), predicates);
    }

    /**
     * Checks if the listener should be invoked for the given event.
     *
     * @param event the event to check.
     * @return {@code true} if the listener should be invoked, {@code false} otherwise.
     */
    public boolean shouldInvoke(Event event) {
        return predicates.stream().allMatch(predicate -> predicate.test(event));
    }
}
