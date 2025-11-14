package github.businessdirt.jasper.reflections;

import github.businessdirt.jasper.reflections.scanners.ClasspathScanner;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A simple classpath scanner that finds all classes in a given package.
 * It works for both file systems (IDE) and JAR files (production).
 */
@SuppressWarnings("unused")
public class Reflections {

    private final Set<Class<?>> foundClasses;

    /**
     * Constructs a new {@link Reflections} instance and scans the given base package
     * for all classes.
     *
     * @param basePackage the base package to scan (e.g., "com.example.myproject").
     * @throws IOException if an I/O error occurs during scanning.
     */
    public Reflections(
            @NotNull String basePackage
    ) throws IOException {
        this(basePackage, null);
    }

    /**
     * Constructs a new {@link Reflections} instance and scans the given base package
     * for all classes.
     *
     * @param basePackage the base package to scan (e.g., "com.example.myproject").
     * @param logger a logger to log errors to
     * @throws IOException if an I/O error occurs during scanning.
     */
    public Reflections(
            @NotNull String basePackage,
            @Nullable Logger logger
    ) throws IOException {
        ClasspathScanner scanner = new ClasspathScanner(basePackage, logger);
        this.foundClasses = scanner.scan();
    }

    /**
     * Returns an immutable set of all classes found during the scan.
     *
     * @return a {@link Set} of {@link Class} objects.
     */
    public @NotNull Set<Class<?>> getClasses() {
        return this.foundClasses;
    }

    /**
     * Returns a {@link Stream} of all classes found during the scan.
     *
     * @return a {@link Stream} of {@link Class} objects.
     */
    public @NotNull Stream<Class<?>> stream() {
        return this.foundClasses.stream();
    }

    /**
     * Retrieves all public methods annotated with the specified annotation type
     * from the classes found during the scan.
     *
     * @param annotation the annotation class to search for.
     * @return a {@link Set} of {@link Method} objects annotated with the given annotation.
     */
    public @NotNull Set<Method> getMethodsAnnotatedWith(
            @NotNull Class<? extends Annotation> annotation
    ) {
        return this.stream()
                .flatMap(c -> Arrays.stream(c.getMethods())
                        .filter(m -> m.isAnnotationPresent(annotation)))
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves all classes that inherit from the specified class.
     * @param type the superclass.
     * @return the classes that extends or implement the superclass.
     * @param <T> the generic type of the superclass.
     */
    public <T> @NotNull Set<Class<? extends T>> getSubTypesOf(
            @NotNull Class<T> type
    ) {
        //noinspection unchecked
        return this.stream()
                .filter(type::isAssignableFrom)
                .map(cls -> (Class<? extends T>) cls)
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves all classes annotated with the specified annotation.
     * @param annotation the annotation the classes need to be annotated with.
     * @return all classes with the given annotation.
     */
    public @NotNull Set<Class<?>> getClassesAnnotatedWith(
            @NotNull Class<? extends Annotation> annotation
    ) {
        return this.stream()
                .filter(c -> c.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }
}
