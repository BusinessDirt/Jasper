package github.businessdirt.jasper.reflections.scanners;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple classpath scanner that works both for JAR files and when running from a directory
 * @param basePackage the package to scan
 */
public record ClasspathScanner(
        @NotNull String basePackage,
        @Nullable Logger logger
) {

    /**
     * Scans the specified package recursively for classes. This works for running from a directory and from a JAR
     * @return the set of classes this scanner found
     * @throws IOException if an io exception has been found
     */
    public @NotNull Set<Class<?>> scan() throws IOException {
        String packagePath = basePackage.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(packagePath);

        Set<String> classNames = new HashSet<>();

        DirectoryScanner directoryScanner = new DirectoryScanner(this.basePackage, this.logger);
        JARScanner jarScanner = new JARScanner(this.basePackage, this.logger);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();

            if ("file".equals(protocol)) {
                classNames.addAll(directoryScanner.findAll(resource));
            } else if ("jar".equals(protocol)) {
                classNames.addAll(jarScanner.findAll(resource));
            }
        }

        return loadClasses(classNames, classLoader);
    }

    private @NotNull Set<Class<?>> loadClasses(
            @NotNull Set<String> classNames,
            @NotNull ClassLoader classLoader
    ) {
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
                if (logger != null) logger.atWarn().withThrowable(e).log("Could not load class '{}'.", className);
            }
        }
        return loadedClasses;
    }
}
