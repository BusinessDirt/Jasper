/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import java.io.PrintStream;
import java.util.Map;

/**
 * Represents the context in which a command is executed.
 *
 * @param source    the command source
 * @param arguments the command arguments
 */
public record CommandContext<S extends CommandSource>(S source, Map<String, Object> arguments) {

    /**
     * Returns the argument with the given name.
     *
     * @param name  the name of the argument
     * @param clazz the class of the argument
     * @param <T>   the type of the argument
     * @return the argument
     */
    @SuppressWarnings({"unchecked", "unused"})
    public <T> T getArgument(String name, Class<T> clazz) {
        return (T) arguments.get(name);
    }

    /**
     * @return the output stream the commands should use
     */
    public PrintStream out() {
        return this.source.out();
    }
}
