package github.businessdirt.jasper.events.system.exceptions;

import org.jetbrains.annotations.NotNull;

public class ClassNotInstantiableException extends RuntimeException {

    public ClassNotInstantiableException(@NotNull Class<?> cls) {
        super(String.format("No default constructor or INSTANCE field found for %s", cls.getName()));
    }
}
