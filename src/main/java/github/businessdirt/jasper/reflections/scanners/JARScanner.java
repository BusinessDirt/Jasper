package github.businessdirt.jasper.reflections.scanners;

import github.businessdirt.jasper.reflections.exceptions.ScanningException;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A scanner that scans a JAR file for any classes
 * @param basePackage the base package parsed from the ClasspathScanner
 * @param logger an optional logger to log debug events to
 */
public record JARScanner(String basePackage, Logger logger) {

    /**
     * Finds all classes for a given resource.
     * @param resource the resource to scan
     * @return a set of all class names
     */
    public Set<String> findAll(URL resource) {
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
                        if (this.logger != null) this.logger.atDebug().log("Found class '{}' in JAR.", className);
                    }
                }
            }
        } catch (IOException e) {
            throw new ScanningException("Error scanning JAR", e, this.logger);
        }

        return classNames;
    }
}
