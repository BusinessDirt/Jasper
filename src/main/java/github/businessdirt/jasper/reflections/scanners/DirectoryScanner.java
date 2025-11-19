package github.businessdirt.jasper.reflections.scanners;

import github.businessdirt.jasper.reflections.exceptions.ScanningException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A scanner that scans a directory for any classes
 * @param basePackage the base package parsed from the ClasspathScanner
 */
public record DirectoryScanner(
        @NotNull String basePackage
) {

    /**
     * Finds all classes for a given resource.
     * @param resource the resource to scan
     * @return a set of all class names
     */
    @SuppressWarnings("resource")
    public @NotNull Set<String> findAll(@NotNull URL resource) {
        try {
            Path packageDir = Paths.get(resource.toURI());

            // Walk the file tree and find all .class files
            return Files.walk(packageDir)
                    .filter(path -> path.toString().endsWith(".class"))
                    .map(path -> {
                        Path relativePath = packageDir.relativize(path);
                        String relativeName = relativePath.toString();

                        // Remove .class extension
                        String className = relativeName.substring(0, relativeName.length() - 6);

                        // Turn path (com/my/project/MyClass) into package (com.my.project.MyClass)
                        return this.basePackage + "." + className.replace(File.separatorChar, '.');
                    })
                    .collect(Collectors.toSet());

        } catch (Exception e) {
            throw new ScanningException("Error scanning directory", e);
        }
    }
}
