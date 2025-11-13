package github.businessdirt.jasper.events.system;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleEvent {
    /**
     * For cases where the event properties are themselves not needed, and solely a listener for an event fire suffices.
     * To specify multiple events, use eventTypes instead.
     */
    Class<? extends Event> eventType() default Event.class;

    /**
     * The priority of when the event will be called, lower priority will be called first, see the companion object.
     */
    Priority priority() default Priority.MEDIUM;

    /**
     * If the event is cancelled & receiveCancelled is true, then the method will still invoke.
     */
    boolean receiveCancelled() default false;

    enum Priority {
        HIGHEST(-2),
        HIGH(-1),
        MEDIUM(0),
        LOW(1),
        LOWEST(2);

        private final int priority;

        Priority(int priority) {
            this.priority = priority;
        }

        public int asInt() {
            return this.priority;
        }
    }
}
