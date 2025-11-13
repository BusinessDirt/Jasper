package github.businessdirt.jasper.reflections;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class LambdaFactory {

    @SuppressWarnings("unchecked")
    public static Consumer<Object> createConsumerFromMethod(Object instance, Method method) {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle handle = lookup.unreflect(method);
            CallSite site = LambdaMetafactory.metafactory(
                    lookup,
                    "accept",
                    MethodType.methodType(Consumer.class, instance.getClass()),
                    MethodType.methodType(void.class, Object.class),
                    handle,
                    MethodType.methodType(void.class, method.getParameterTypes()[0])
            );
            return (Consumer<Object>) site.getTarget().bindTo(instance).invokeExact();
        } catch (Throwable e) {
            throw new IllegalArgumentException("Method " + instance.getClass().getName() + "#" + method.getName() + " is not a valid consumer", e);
        }
    }

    public static Runnable createRunnableFromMethod(Object instance, Method method) {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle handle = lookup.unreflect(method);
            CallSite site = LambdaMetafactory.metafactory(
                    lookup,
                    "run",
                    MethodType.methodType(Runnable.class, instance.getClass()),
                    MethodType.methodType(void.class),
                    handle,
                    MethodType.methodType(void.class)
            );
            return (Runnable) site.getTarget().bindTo(instance).invokeExact();
        } catch (Throwable e) {
            throw new IllegalArgumentException("Method " + instance.getClass().getName() + "#" + method.getName() + " is not a valid runnable", e);
        }
    }

    private Consumer<Object> createZeroParameterConsumer(Method method, Object instance) {
        Runnable runnable = createRunnableFromMethod(instance, method);
        return _ -> runnable.run();
    }

    private Consumer<Object> createSingleParameterConsumer(Method method, Object instance) {
        return createConsumerFromMethod(instance, method);
    }
}
