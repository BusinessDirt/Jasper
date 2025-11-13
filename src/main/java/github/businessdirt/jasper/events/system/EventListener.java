package github.businessdirt.jasper.events.system;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventListener {
    public final String name;
    public final Consumer<Object> invoker;
    public final HandleEvent.Priority priority;
    public final boolean canReceiveCancelled;

    private final List<Predicate<Event>> predicates;

    public EventListener(
            String name,
            Consumer<Object> invoker,
            HandleEvent options,
            List<Predicate<Event>> extraPredicates
    ) {
        this.name = name;
        this.invoker = invoker;
        this.priority = options.priority();
        this.canReceiveCancelled = options.receiveCancelled();

        this.predicates = new ArrayList<>();
        if (!canReceiveCancelled) this.predicates.add(event -> !event.isCancelled());

        this.predicates.addAll(extraPredicates);
    }

    public boolean shouldInvoke(Event event) {
        return predicates.stream().allMatch(predicate -> predicate.test(event));
    }
}
