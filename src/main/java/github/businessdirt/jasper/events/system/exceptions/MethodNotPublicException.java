package github.businessdirt.jasper.events.system.exceptions;

import github.businessdirt.jasper.reflections.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.lang.reflect.Method;

public class MethodNotPublicException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MethodNotPublicException(@NotNull Method method) {
        super(String.format("Method %s is not public", ReflectionUtils.getMethodString(method)));
    }
}
