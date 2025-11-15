package github.businessdirt.jasper.commands.arguments;

import github.businessdirt.jasper.commands.StringReader;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull Integer parse(@NotNull StringReader reader) throws NumberFormatException {
        return Integer.parseInt(reader.readString());
    }
}
