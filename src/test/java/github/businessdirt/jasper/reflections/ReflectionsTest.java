package github.businessdirt.jasper.reflections;

import github.businessdirt.jasper.reflections.dummies.DummyClassA;
import github.businessdirt.jasper.reflections.dummies.DummyClassB;
import github.businessdirt.jasper.reflections.dummies.DummyInterface;
import github.businessdirt.jasper.reflections.dummies.nested.DummyClassC;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionsTest {

    @Test
    @DisplayName("Should find all classes in a directory (IDE mode)")
    void testScanDirectory() throws IOException {
        // When we run this in the IDE, these classes are loose .class files
        Reflections reflections = new Reflections("github.businessdirt.jasper.reflections.dummies");
        Set<Class<?>> foundClasses = reflections.found();

        assertNotNull(foundClasses);
        assertEquals(4, foundClasses.size());

        assertAll(
                () -> assertTrue(foundClasses.contains(DummyClassA.class)),
                () -> assertTrue(foundClasses.contains(DummyClassB.class)),
                () -> assertTrue(foundClasses.contains(DummyInterface.class)),
                () -> assertTrue(foundClasses.contains(DummyClassC.class))
        );
    }

    @Test
    @DisplayName("Should find classes in a JAR file (Production mode)")
    void testScanJar() throws IOException {
        Reflections reflections = new Reflections("org.junit.jupiter.api");
        Set<Class<?>> foundClasses = reflections.found();

        assertNotNull(foundClasses);
        assertTrue(foundClasses.size() > 20); // Safe bet

        // We can be certain some core classes are in there
        assertAll(
                () -> assertTrue(foundClasses.contains(org.junit.jupiter.api.Test.class)),
                () -> assertTrue(foundClasses.contains(org.junit.jupiter.api.BeforeEach.class)),
                () -> assertTrue(foundClasses.contains(org.junit.jupiter.api.DisplayName.class))
        );
    }

    @Test
    @DisplayName("Should return empty set for a non-existent package")
    void testScanNonExistentPackage() throws IOException {
        Reflections reflections = new Reflections("com.this.is.not.a.real.package.foo.bar");
        Set<Class<?>> foundClasses = reflections.found();

        assertNotNull(foundClasses);
        assertTrue(foundClasses.isEmpty());
    }
}