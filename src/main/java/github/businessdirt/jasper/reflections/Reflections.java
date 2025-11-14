package github.businessdirt.jasper.reflections;

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
public class Reflections {

    private final Set<Class<?>> classes;

    /**
     * Constructs a new {@link Reflections} instance and scans the given base package
     * for all classes.
     *
     * @param basePackage the base package to scan (e.g., "com.example.myproject").
     * @throws IOException if an I/O error occurs during scanning.
     */
    public Reflections(String basePackage) throws IOException {
        ClasspathScanner scanner = new ClasspathScanner(basePackage);
        this.classes = scanner.scan();
    }

    /**
     * Returns an immutable set of all classes found during the scan.
     *
     * @return a {@link Set} of {@link Class} objects.
     */
    public Set<Class<?>> found() {
        return this.classes;
    }

    /**
     * Returns a {@link Stream} of all classes found during the scan.
     *
     * @return a {@link Stream} of {@link Class} objects.
     */
    public Stream<Class<?>> stream() {
        return this.classes.stream();
    }

    /**
     * Retrieves all public methods annotated with the specified annotation type
     * from the classes found during the scan.
     *
     * @param annotation the annotation class to search for.
     * @return a {@link Set} of {@link Method} objects annotated with the given annotation.
     */
    public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        return this.stream()
                .flatMap(c -> Arrays.stream(c.getMethods())
                        .filter(m -> m.isAnnotationPresent(annotation)))
                .collect(Collectors.toSet());
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        //noinspection unchecked
        return this.stream()
                .filter(type::isAssignableFrom)
                .map(cls -> (Class<? extends T>) cls)
                .collect(Collectors.toSet());
    }
}
