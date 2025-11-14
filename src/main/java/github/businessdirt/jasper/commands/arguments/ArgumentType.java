/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands.arguments;

import github.businessdirt.jasper.commands.StringReader;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a type of command argument.
 *
 * @param <T> the type of the argument
 */
public interface ArgumentType<T> {

    /**
     * Parses a string into an argument of type T.
     *
     * @param reader the string to parse
     * @return the parsed argument
     */
    @NotNull T parse(@NotNull StringReader reader);
}
