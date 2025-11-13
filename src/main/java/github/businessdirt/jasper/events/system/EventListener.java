package github.businessdirt.jasper.events.system;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventListener {
    private final String name;
    private final Consumer<Object> invoker;
    private final HandleEvent.Priority priority;
    private final boolean canReceiveCancelled;

    private final List<Predicate<Event>> predicates;

    public EventListener(
            String name,
            Consumer<Object> invoker,
            HandleEvent options
    ) {
        this.name = name;
        this.invoker = invoker;
        this.priority = options.priority();
        this.canReceiveCancelled = options.receiveCancelled();

        this.predicates = new ArrayList<>();
        if (!canReceiveCancelled) this.predicates.add(event -> !event.isCancelled());
    }

    public boolean shouldInvoke(Event event) {
        return predicates.stream().allMatch(predicate -> predicate.test(event));
    }

    public String getName() {
        return name;
    }

    public Consumer<Object> getInvoker() {
        return invoker;
    }

    public HandleEvent.Priority getPriority() {
        return priority;
    }

    public boolean canReceiveCancelled() {
        return canReceiveCancelled;
    }
}
