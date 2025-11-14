package github.businessdirt.jasper.reflections;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static @NotNull String getParameterString(@NotNull Method method) {
        if (method.getParameterCount() == 0) return "";

        return Arrays.stream(method.getParameters())
                .map(Parameter::toString)
                .collect(Collectors.joining(", "));
    }

    public static @NotNull String getMethodString(@NotNull Method method) {
        return method.getDeclaringClass().getName() + "::" + method.getName() + "(" +
                ReflectionUtils.getParameterString(method) + ")";
    }
}
