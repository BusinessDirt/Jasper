/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands.arguments;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.StringReader;

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
