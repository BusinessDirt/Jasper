/* (C) 2025 Maximilian Bollschweiler */
package bollschweiler.de.lmu.ifi.cip.gitlab2.commands.arguments;

import bollschweiler.de.lmu.ifi.cip.gitlab2.commands.StringReader;

/**
 * Represents an integer argument type. This argument type will consume a single word and parse it as an integer.
 */
public class IntegerArgumentType implements ArgumentType<Integer> {

    /**
     * Parses a string into an integer.
     *
     * @param reader the string reader to parse
     * @return the parsed integer
     * @throws NumberFormatException if the string is not a valid integer
     */
    @Override
    public Integer parse(StringReader reader) throws NumberFormatException {
        return Integer.parseInt(reader.readString());
    }
}
