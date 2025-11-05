/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands.arguments;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.StringReader;

/**
 * Represents a string argument type. This argument type will consume a single word and return it as a string.
 */
public class StringArgumentType implements ArgumentType<String> {

    /**
     * Parses a string from the reader.
     *
     * @param reader the string reader to parse
     * @return the parsed string
     */
    @Override
    public String parse(StringReader reader) {
        return reader.readString();
    }
}
