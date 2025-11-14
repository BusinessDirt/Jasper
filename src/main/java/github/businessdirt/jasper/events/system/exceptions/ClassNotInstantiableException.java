package github.businessdirt.jasper.events.system.exceptions;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class ClassNotInstantiableException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ClassNotInstantiableException(@NotNull Class<?> cls) {
        super(String.format("No default constructor or INSTANCE field found for %s", cls.getName()));
    }
}
