package github.businessdirt.jasper.reflections;

import github.businessdirt.jasper.reflections.exceptions.InvalidConsumerException;
import github.businessdirt.jasper.reflections.exceptions.InvalidRunnableException;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * A factory for creating lambda expressions ({@link Consumer} or {@link Runnable}) from methods using
 * {@link LambdaMetafactory}. This allows for efficient invocation of methods discovered via reflection.
 */
public class LambdaFactory {

    /**
     * Creates a {@link Consumer} that invokes the given method on the provided instance.
     * The method must be public, non-static, and take exactly one argument.
     *
     * @param instance the object on which the method will be invoked.
     * @param method   the method to be invoked.
     * @return a {@link Consumer} that, when called, invokes the specified method.
     * @throws InvalidConsumerException if the method is not a valid consumer (e.g., wrong number of arguments, static).
     */
    @SuppressWarnings("unchecked")
    public static @NotNull Consumer<Object> createConsumerFromMethod(
            @NotNull Object instance,
            @NotNull Method method
    ) throws InvalidConsumerException {
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
            throw new InvalidConsumerException(method, e);
        }
    }

    /**
     * Creates a {@link Runnable} that invokes the given method on the provided instance.
     * The method must be public, non-static, and take no arguments.
     *
     * @param instance the object on which the method will be invoked.
     * @param method   the method to be invoked.
     * @return a {@link Runnable} that, when called, invokes the specified method.
     * @throws InvalidRunnableException if the method is not a valid runnable (e.g., wrong number of arguments, static).
     */
    public static @NotNull Runnable createRunnableFromMethod(
            @NotNull Object instance,
            @NotNull Method method
    ) throws InvalidRunnableException {
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
            throw new InvalidRunnableException(method, e);
        }
    }
}
