/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import bollschweiler.de.lmu.ifi.cip.gitlab2.network.client.ClientContext;
import java.util.Map;

/**
 * Represents the context in which a command is executed.
 *
 * @param source    the command source
 * @param arguments the command arguments
 */
public record CommandContext(ClientContext source, Map<String, Object> arguments) {

    /**
     * Returns the source of the command.
     *
     * @return the command source
     */
    @Override
    public ClientContext source() {
        return source;
    }

    /**
     * Returns the argument with the given name.
     *
     * @param name  the name of the argument
     * @param clazz the class of the argument
     * @param <T>   the type of the argument
     * @return the argument
     */
    @SuppressWarnings("unchecked")
    public <T> T getArgument(String name, Class<T> clazz) {
        return (T) arguments.get(name);
    }
}
