package github.businessdirt.jasper.events.system;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event listener.
 * The method must be public and have either 0 or 1 parameter.
 * If it has 1 parameter, the parameter type is the event type it listens for.
 * If it has 0 parameters, the event type must be specified in {@link #eventType()}.
 *
 * @see EventBus
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleEvent {
    /**
     * For cases where the event properties are themselves not needed, and solely a listener for an event fire suffices.
     * This is required when the listener method has 0 parameters.
     *
     * @return the type of event to listen for.
     */
    Class<? extends Event> eventType() default Event.class;

    /**
     * The priority of the event listener. Listeners with higher priority (lower integer value) are called first.
     *
     * @return the priority of the listener.
     */
    Priority priority() default Priority.MEDIUM;

    /**
     * If {@code true}, the listener will be called even if the event has been cancelled.
     *
     * @return {@code true} to receive cancelled events, {@code false} otherwise.
     */
    boolean receiveCancelled() default false;

    /**
     * The priority of an event listener.
     */
    enum Priority {
        /** The highest priority, called first. */
        HIGHEST(-2),
        /** High priority, called after HIGHEST. */
        HIGH(-1),
        /** The default priority. */
        MEDIUM(0),
        /** Low priority, called before LOWEST. */
        LOW(1),
        /** The lowest priority, called last. */
        LOWEST(2);

        private final int priority;

        Priority(int priority) {
            this.priority = priority;
        }

        /**
         * @return the integer value of the priority.
         */
        public int asInt() {
            return this.priority;
        }
    }
}
