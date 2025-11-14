package github.businessdirt.jasper.events.system;

import github.businessdirt.jasper.events.system.exceptions.ClassNotInstantiableException;
import github.businessdirt.jasper.events.system.exceptions.EventBusNotInitializedException;
import github.businessdirt.jasper.events.system.exceptions.MethodNotPublicException;
import github.businessdirt.jasper.events.system.exceptions.ParameterException;
import github.businessdirt.jasper.reflections.LambdaFactory;
import github.businessdirt.jasper.reflections.ReflectionUtils;
import github.businessdirt.jasper.reflections.Reflections;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The main class for the event system.
 * <p>
 * The EventBus discovers event listeners in a given package and registers them.
 * It is responsible for posting events to the appropriate listeners.
 * <p>
 * The EventBus must be initialized with {@link #initialize(String, Logger)} before use.
 * After initialization, the singleton instance can be retrieved with {@link #get()}.
 */
public class EventBus {

    private static EventBus INSTANCE;

    private final Map<Class<? extends Event>, List<EventListener>> listeners;
    private final Map<Class<? extends Event>, EventHandler> handlers;
    private final Logger logger;

    private EventBus(
            String basePackage,
            Logger logger
    ) throws IOException, MethodNotPublicException, ClassNotInstantiableException, ParameterException {
        this.listeners = new HashMap<>();
        this.handlers = new HashMap<>();
        this.logger = logger;

        Reflections reflections = new Reflections(basePackage);
        Set<Method> annotatedMethods = reflections.getMethodsAnnotatedWith(HandleEvent.class);

        Map<Class<?>, Object> instances = new HashMap<>();

        annotatedMethods.forEach(method -> {
            method.setAccessible(true);

            if (!Modifier.isPublic(method.getModifiers()))
                throw new MethodNotPublicException(method, this.logger);

            var name = ReflectionUtils.getMethodString(method);
            var instance = this.getInstance(method, instances); // throws ClassNotInstantiableException
            var eventData = this.getEventData(method); // throws ParameterException
            var eventConsumer = this.getEventConsumer(method, instance); // throws ParameterException

            listeners.computeIfAbsent(eventData.getValue(), _ -> new ArrayList<>())
                    .add(EventListener.of(name, eventConsumer, eventData.getKey()));
        });
    }

    private @Nonnull Object getInstance(
            Method method,
            Map<Class<?>, Object> instances
    ) throws ClassNotInstantiableException {
        return instances.computeIfAbsent(method.getDeclaringClass(), _ -> {
            try {
                return method.getDeclaringClass().getField("INSTANCE").get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                try {
                    return method.getDeclaringClass().getConstructor().newInstance();
                } catch (Exception ex) {
                    throw new ClassNotInstantiableException(method.getDeclaringClass(), this.logger);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private @Nonnull Map.Entry<HandleEvent, Class<? extends Event>> getEventData(
            Method method
    ) throws ParameterException {
        HandleEvent options = method.getAnnotation(HandleEvent.class);

        // options shouldn't be null because only methods annotated
        // with HandleEvent are being parsed into this function
        if (options == null && this.logger != null) {
            this.logger.atWarn().log("Method {} is annotated with @HandleEvent but the annotation object is null",
                    ReflectionUtils.getMethodString(method));
        }

        assert options != null;

        return switch (method.getParameterCount()) {
            case 1 -> {
                Class<?> eventType = method.getParameterTypes()[0];
                if(!Event.class.isAssignableFrom(eventType))
                    throw new ParameterException(method, "must be a subtype of " + Event.class.getName(), this.logger);

                yield Map.entry(options, (Class<? extends Event>) eventType);
            }

            case 0 -> {
                if (options.eventType() == Event.class) throw new ParameterException(method,
                        "Specify eventType in @HandleEvent if using 0 parameters", this.logger);

                yield Map.entry(options, options.eventType());
            }

            default -> throw new ParameterException(method, "must have either 0 or 1 parameters", this.logger);
        };
    }

    private @Nonnull Consumer<Object> getEventConsumer(
            Method method,
            Object instance
    ) throws ParameterException {
        return switch (method.getParameterCount()) {
            case 1 -> LambdaFactory.createConsumerFromMethod(instance, method);
            case 0 -> _ -> LambdaFactory.createRunnableFromMethod(instance, method).run();

            default -> throw new ParameterException(method, "must have either 0 or 1 parameters", this.logger);
        };
    }

    /**
     * Gets the {@link EventHandler} for the given event class.
     * If an event handler does not exist for the given event, a new one is created.
     *
     * @param event the event class.
     * @return the {@link EventHandler} for the given event class.
     */
    public EventHandler getEventHandler(Class<? extends Event> event) {
        return handlers.computeIfAbsent(event, e ->
                new EventHandler(e, getEventClasses(e).stream()
                        .map(cls -> listeners.getOrDefault(cls, Collections.emptyList()))
                        .flatMap(List::stream)
                        .collect(Collectors.toList())
                )
        );
    }

    private List<Class<?>> getEventClasses(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<>();
        classes.add(clazz);

        var currentClass = clazz;
        while (currentClass.getSuperclass() != null) {
            var superClass = currentClass.getSuperclass();
            if (superClass == Event.class) break;
            if (superClass == CancellableEvent.class) break;
            classes.add(superClass);
            currentClass = superClass;
        }

        return classes;
    }

    /**
     * Initializes the singleton instance of the {@link EventBus}.
     * This method must be called before {@link #get()} is called.
     * <p>The {@link EventBus} will scan all classes in the give package for listener methods
     * i.e. methods annotated with {@link HandleEvent}.
     * These methods will then be called when a corresponding event is posted.</p>
     *
     * @param basePackage the package to scan for event listeners.
     * @param logger      the logger to use for logging errors. Can be null.
     *
     * @throws IOException if an I/O error occurs during initialization. This is thrown by {@link Reflections}.
     *
     * @throws MethodNotPublicException if a listener method is not public.
     * @throws ClassNotInstantiableException if a class that has a listener method has no default constructor and no INSTANCE field.
     * @throws ParameterException if there is an error with the parameters of a listener method.
     */

    public static void initialize(
            String basePackage,
            Logger logger
    ) throws IOException, MethodNotPublicException, ClassNotInstantiableException, ParameterException {
        if (INSTANCE == null) {
            INSTANCE = new EventBus(basePackage, logger);
        }
    }

    /**
     * Gets the singleton instance of the {@link EventBus}.
     * {@link #initialize(String, Logger)} must be called before this method.
     *
     * @return the singleton instance of the {@link EventBus}.
     * @throws EventBusNotInitializedException if the EventBus has not been initialized.
     */
    public static EventBus get() throws EventBusNotInitializedException {
        if (INSTANCE == null) throw new EventBusNotInitializedException();
        return INSTANCE;
    }
}
