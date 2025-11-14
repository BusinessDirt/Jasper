package github.businessdirt.jasper.events.system;

import github.businessdirt.jasper.events.system.exceptions.*;
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

    /**
     * Constructs a new {@link EventBus}.
     * This constructor is private. Use {@link #initialize(String, Logger)} to create an instance.
     *
     * @param basePackage the package to scan for event listeners.
     * @param logger      the logger to use for logging errors.
     * @throws IOException if an I/O error occurs during initialization. This is thrown by {@link Reflections}
     * @throws ClassNotInstantiableException if a class that has a method annotated with {@link HandleEvent}
     * has no default constructor and no INSTANCE field.
     * @throws InvalidParameterCountException if there is an error with the parameter count of an annotated method
     * @throws MethodNotPublicException if a method annotated with {@link HandleEvent} is not public
     */
    private EventBus(String basePackage, Logger logger) throws IOException {
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

            var instance = this.getInstance(method, instances);
            var name = this.buildListenerName(method);
            var eventData = this.getEventData(method);
            var eventConsumer = this.getEventConsumer(method, instance);

            listeners.computeIfAbsent(eventData.getValue(), _ -> new ArrayList<>())
                    .add(EventListener.of(name, eventConsumer, eventData.getKey()));
        });
    }

    private @Nonnull Consumer<Object> getEventConsumer(Method method, Object instance) {
        return switch (method.getParameterCount()) {
            case 1 -> LambdaFactory.createConsumerFromMethod(instance, method);
            case 0 -> _ -> LambdaFactory.createRunnableFromMethod(instance, method).run();

            default -> throw new InvalidParameterCountException(method, "Must have either 0 or 1 parameter", this.logger);
        };
    }

    private @Nonnull Object getInstance(Method method, Map<Class<?>, Object> instances) {
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

    @SuppressWarnings("unchecked")
    private @Nonnull Map.Entry<HandleEvent, Class<? extends Event>> getEventData(Method method) {
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
                    throw new ParameterNotEventException(method, this.logger);

                yield Map.entry(options, (Class<? extends Event>) eventType);
            }

            case 0 -> {
                if (options.eventType() == Event.class) throw new InvalidParameterCountException(method,
                        "Specify eventType in @HandleEvent if using 0 parameters", this.logger);

                yield Map.entry(options, options.eventType());
            }

            default -> throw new InvalidParameterCountException(method, "Must have either 0 or 1 parameter", this.logger);
        };
    }

    private String buildListenerName(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuilder paramTypesString = new StringBuilder("(");

        for (int i = 0; i < parameterTypes.length; i++) {
            if (i > 0) paramTypesString.append(", ");
            paramTypesString.append(parameterTypes[i].getSimpleName());
        }

        paramTypesString.append(")");

        return method.getDeclaringClass().getName() + "::" + method.getName() + paramTypesString;
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

    private void error(Throwable throwable, String message, Object... args) {
        if (this.logger == null) return;

        if (throwable == null) {
            this.logger.error(message, args);
            return;
        }

        this.logger.atError()
                .withThrowable(throwable)
                .log(message, args);
    }

    /**
     * Initializes the singleton instance of the {@link EventBus}.
     * This method must be called before {@link #get()} is called.
     *
     * @param basePackage the package to scan for event listeners.
     * @param logger      the logger to use for logging errors.
     * @throws IOException if an I/O error occurs during initialization
     */
    public static void initialize(String basePackage, Logger logger) throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new EventBus(basePackage, logger);
        }
    }

    /**
     * Gets the singleton instance of the {@link EventBus}.
     * {@link #initialize(String, Logger)} must be called before this method.
     *
     * @return the singleton instance of the {@link EventBus}.
     * @throws EventBusNotInitializedException if the EventBus has not been initialized. This is thrown by {@link Reflections}
     */
    public static EventBus get() throws EventBusNotInitializedException{
        if (INSTANCE == null) throw new EventBusNotInitializedException();
        return INSTANCE;
    }
}
