package github.businessdirt.jasper.events.system;

import github.businessdirt.jasper.reflections.LambdaFactory;
import github.businessdirt.jasper.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EventBus {

    private static EventBus INSTANCE;

    private final Map<Class<? extends Event>, List<EventListener>> listeners;
    private final Map<Class<? extends Event>, EventHandler> handlers;

    private EventBus(String basePackage) {
        this.listeners = new HashMap<>();
        this.handlers = new HashMap<>();

        try {
            Reflections reflections = new Reflections(basePackage);
            Set<Method> annotatedMethods = reflections.getMethodsAnnotatedWith(HandleEvent.class);

            Map<Class<?>, Object> instances = new HashMap<>();

            annotatedMethods.forEach(method -> {
                method.setAccessible(true);

                Object instance = instances.computeIfAbsent(method.getDeclaringClass(), _ -> {
                    try {
                        return method.getDeclaringClass().getField("INSTANCE").get(null);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        try {
                            return method.getDeclaringClass().getConstructor().newInstance();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

                Map.Entry<HandleEvent, Class<? extends Event>> eventData = getEventData(method);
                if (eventData == null) return;

                if (!Modifier.isPublic(method.getModifiers())) {
                    throw new RuntimeException(
                            "Method " + method.getName() + "() in " + instance.getClass().getName() + " is not public. Make sure to set it to public."
                    ); // TODO
                }

                String name = this.buildListenerName(method);
                Consumer<Object> eventConsumer = switch (method.getParameterCount()) {
                    case 1 -> LambdaFactory.createConsumerFromMethod(instance, method);
                    case 0 -> _ -> LambdaFactory.createRunnableFromMethod(instance, method).run();

                    default -> throw new IllegalArgumentException(
                            "Method " + method.getName() + "() must have either 0 or 1 parameter"
                    ); // TODO
                };

                listeners.computeIfAbsent(eventData.getValue(), _ -> new ArrayList<>())
                        .add(EventListener.of(name, eventConsumer, eventData.getKey()));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
    private Map.Entry<HandleEvent, Class<? extends Event>> getEventData(Method method) {
        HandleEvent options = method.getAnnotation(HandleEvent.class);
        if (options == null) return null;

        return switch (method.getParameterCount()) {
            case 1 -> {
                Class<?> eventType = method.getParameterTypes()[0];
                if(!Event.class.isAssignableFrom(eventType)) {
                    throw new RuntimeException(
                            "Method " + method.getName() + " parameter must be a subclass of Event."
                    ); // TODO
                }

                yield Map.entry(options, (Class<? extends Event>) eventType);
            }

            case 0 -> {
                if (options.eventType() == Event.class) {
                    throw new RuntimeException(
                            "Method " + method.getName() + "must have an event type specified in @HandleEvent."
                    ); //TODO
                }

                yield Map.entry(options, options.eventType());
            }

            default -> null;
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

    public static void initialize(String basePackage) {
        if (INSTANCE == null) {
            INSTANCE = new EventBus(basePackage);
        }
    }

    public static EventBus get() {
        if (INSTANCE == null) throw new RuntimeException("Use EventBus#initialize(String basePackage) to initialize the EventBus first!");
        return INSTANCE;
    }
}
