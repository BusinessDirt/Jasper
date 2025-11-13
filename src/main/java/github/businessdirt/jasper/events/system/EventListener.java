package github.businessdirt.jasper.events.system;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public record EventListener(
    String name,
    Consumer<Object> invoker,
    HandleEvent.Priority priority,
    boolean canReceiveCancelled,
    List<Predicate<Event>> predicates
) {

    public static EventListener of(
            String name,
            Consumer<Object> invoker,
            HandleEvent options
    ) {
        List<Predicate<Event>> predicates = new ArrayList<>();
        if (!options.receiveCancelled()) predicates.add(event -> !event.isCancelled());
        return new EventListener(name, invoker, options.priority(), options.receiveCancelled(), predicates);
    }

    public boolean shouldInvoke(Event event) {
        return predicates.stream().allMatch(predicate -> predicate.test(event));
    }
}
