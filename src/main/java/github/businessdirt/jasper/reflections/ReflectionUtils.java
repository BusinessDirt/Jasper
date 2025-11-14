package github.businessdirt.jasper.reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static String getParameterString(Method method) {
        if (method.getParameterCount() == 0) return "";
        return Arrays.stream(method.getParameters())
                .map(Parameter::toString)
                .collect(Collectors.joining(", "));
    }

    public static String getMethodString(Method method) {
        return method.getDeclaringClass().getName() + "::" + method.getName() + "(" + getParameterString(method) + ")";
    }
}
