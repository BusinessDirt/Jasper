package github.businessdirt.jasper.reflections;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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
        this.classes = this.scan(basePackage);
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

    private Set<Class<?>> scan(String basePackage) throws IOException {
        String packagePath = basePackage.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(packagePath);

        Set<String> classNames = new HashSet<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();

            if ("file".equals(protocol)) {
                // Running from a directory (e.g., in an IDE)
                classNames.addAll(findClassesInDirectory(resource, basePackage));
            } else if ("jar".equals(protocol)) {
                // Running from a JAR file
                classNames.addAll(findClassesInJar(resource));
            }
        }

        return loadClasses(classNames, classLoader);
    }

    @SuppressWarnings("resource")
    private Set<String> findClassesInDirectory(URL resource, String basePackage) {
        try {
            Path packageDir = Paths.get(resource.toURI());
            // Walk the file tree and find all .class files
            return Files.walk(packageDir)
                    .filter(path -> path.toString().endsWith(".class"))
                    .map(path -> toClassName(packageDir, path, basePackage))
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Error scanning directory", e);
        }
    }

    private String toClassName(Path baseDir, Path classFile, String basePackage) {
        Path relativePath = baseDir.relativize(classFile);
        String relativeName = relativePath.toString();
        // Remove .class extension
        String className = relativeName.substring(0, relativeName.length() - 6);
        // Turn path (com/my/project/MyClass) into package (com.my.project.MyClass)
        return basePackage + "." + className.replace(File.separatorChar, '.');
    }

    private Set<String> findClassesInJar(URL resource) {
        Set<String> classNames = new HashSet<>();
        try {
            URLConnection con = resource.openConnection();
            if (con instanceof JarURLConnection) {
                JarFile jarFile = ((JarURLConnection) con).getJarFile();
                String packagePath = ((JarURLConnection) con).getEntryName();

                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();

                    if (name.startsWith(packagePath) && name.endsWith(".class") && !entry.isDirectory()) {
                        // Turn path (com/my/project/MyClass.class) into package
                        String className = name.substring(0, name.length() - 6).replace('/', '.');
                        classNames.add(className);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error scanning JAR", e);
        }
        return classNames;
    }

    private Set<Class<?>> loadClasses(Set<String> classNames, ClassLoader classLoader) {
        Set<Class<?>> loadedClasses = new HashSet<>();
        for (String className : classNames) {
            try {
                // Load the class but do NOT initialize it (runs static blocks)
                // This is safer and faster.
                Class<?> clazz = Class.forName(className, false, classLoader);
                loadedClasses.add(clazz);
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                // This is common. The class might have dependencies that
                // aren't available. We can just skip it.
                System.err.println("Could not load class " + className + ": " + e.getMessage());
            }
        }
        return loadedClasses;
    }
}
