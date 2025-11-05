/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands.arguments;

import github.businessdirt.jasper.commands.StringReader;

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
    T parse(StringReader reader);
}
