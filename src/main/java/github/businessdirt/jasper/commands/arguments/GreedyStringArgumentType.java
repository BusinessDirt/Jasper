/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands.arguments;

import github.businessdirt.jasper.commands.StringReader;

/**
 * Represents a greedy string argument type. This argument type will consume the rest of the command string.
 */
public class GreedyStringArgumentType implements ArgumentType<String> {

    /**
     * Parses the remaining of the reader and returns it as a string.
     *
     * @param reader the string reader to parse
     * @return the parsed string
     */
    @Override
    public String parse(StringReader reader) {
        return reader.readRemaining();
    }
}
